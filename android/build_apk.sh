#!/bin/bash
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$DIR")"
SDK_DIR="${ANDROID_SDK_ROOT:-$HOME/Library/Android/sdk}"
BUILD_TOOLS_DIR="$SDK_DIR/build-tools/36.0.0"
PLATFORM_JAR="$SDK_DIR/platforms/android-37.0/android.jar"

echo "==> Cleaning old build artifacts..."
rm -rf "$DIR/build"
mkdir -p "$DIR/build/obj" "$DIR/build/dex" "$DIR/build/apk"

# 1. Compile resources with aapt2
echo "==> Compiling resources..."
"$BUILD_TOOLS_DIR/aapt2" compile --dir "$DIR/app/src/main/res" -o "$DIR/build/res.zip"

# 2. Link resources and generate R.java with explicit min/target SDK and versioning
echo "==> Linking resources with Target SDK 34..."
"$BUILD_TOOLS_DIR/aapt2" link \
    -o "$DIR/build/app-unsigned.apk" \
    -I "$PLATFORM_JAR" \
    --manifest "$DIR/app/src/main/AndroidManifest.xml" \
    --java "$DIR/app/src/main/java" \
    --min-sdk-version 26 \
    --target-sdk-version 34 \
    --version-code 1 \
    --version-name "1.0" \
    "$DIR/build/res.zip" \
    --auto-add-overlay

# 3. Compile Java sources
echo "==> Compiling Java sources..."
JAVA_FILES=$(find "$DIR/app/src/main/java" -name "*.java")
javac -cp "$PLATFORM_JAR" -d "$DIR/build/obj" $JAVA_FILES

# 4. Dex with d8
echo "==> Compiling Dalvik bytecode (dex)..."
CLASS_FILES=$(find "$DIR/build/obj" -name "*.class")
"$BUILD_TOOLS_DIR/d8" --lib "$PLATFORM_JAR" --min-api 26 --output "$DIR/build/dex" $CLASS_FILES

# 5. Package classes.dex into APK
echo "==> Packaging classes.dex..."
jar uf "$DIR/build/app-unsigned.apk" -C "$DIR/build/dex" classes.dex

# 6. Zipalign (4-byte alignment)
echo "==> Zip aligning..."
"$BUILD_TOOLS_DIR/zipalign" -f -p 4 "$DIR/build/app-unsigned.apk" "$DIR/build/app-aligned.apk"

# 7. Keystore check
if [ ! -f "$DIR/debug.keystore" ]; then
    echo "==> Generating debug keystore..."
    keytool -genkey -v -keystore "$DIR/debug.keystore" -storepass android \
        -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 \
        -validity 10000 -dname "CN=Android Debug,O=Android,C=US"
fi

# 8. Sign APK with v1, v2, and v3 schemes
echo "==> Signing APK..."
"$BUILD_TOOLS_DIR/apksigner" sign \
    --ks "$DIR/debug.keystore" \
    --ks-pass pass:android \
    --key-pass pass:android \
    --v1-signing-enabled true \
    --v2-signing-enabled true \
    --v3-signing-enabled true \
    --out "$ROOT_DIR/SmartStudyPlanner.apk" \
    "$DIR/build/app-aligned.apk"

# 9. Verify signatures
echo "==> Verifying APK..."
"$BUILD_TOOLS_DIR/apksigner" verify -v "$ROOT_DIR/SmartStudyPlanner.apk"

echo "=========================================================="
echo "SUCCESS! Valid Android APK created at:"
echo "$ROOT_DIR/SmartStudyPlanner.apk"
echo "=========================================================="
