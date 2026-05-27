#!/usr/bin/env python3
"""
Generate a keystore for DeharPlayer APK signing
"""

import os
import subprocess
import sys

KEYSTORE_PATH = r"E:\Dehar Player update\DeharPlayer\deharplayer.keystore"
KEYSTORE_PASSWORD = "deharplayer123"
KEY_ALIAS = "deharplayer"
KEY_PASSWORD = "deharplayer123"
VALIDITY_DAYS = "10000"

def generate_keystore():
    """Generate a new keystore using keytool"""
    
    if os.path.exists(KEYSTORE_PATH):
        print(f"[✓] Keystore already exists: {KEYSTORE_PATH}")
        return True
    
    print(f"[*] Generating keystore: {KEYSTORE_PATH}")
    print(f"[*] Alias: {KEY_ALIAS}")
    print(f"[*] Validity: {VALIDITY_DAYS} days")
    
    cmd = [
        "keytool",
        "-genkey",
        "-v",
        "-keystore", KEYSTORE_PATH,
        "-keyalg", "RSA",
        "-keysize", "2048",
        "-validity", VALIDITY_DAYS,
        "-alias", KEY_ALIAS,
        "-storepass", KEYSTORE_PASSWORD,
        "-keypass", KEY_PASSWORD,
        "-dname", "CN=DeharPlayer,OU=Development,O=DeharPlayer,L=India,S=India,C=IN"
    ]
    
    try:
        result = subprocess.run(cmd, capture_output=True, text=True, check=False)
        
        if result.returncode == 0:
            print("[✓] Keystore generated successfully!")
            return True
        else:
            print(f"[✗] Error generating keystore:")
            print(f"STDOUT: {result.stdout}")
            print(f"STDERR: {result.stderr}")
            return False
            
    except Exception as e:
        print(f"[✗] Exception while generating keystore: {e}")
        return False

def verify_keystore():
    """Verify the generated keystore"""
    
    if not os.path.exists(KEYSTORE_PATH):
        print(f"[✗] Keystore not found: {KEYSTORE_PATH}")
        return False
    
    print(f"[*] Verifying keystore...")
    
    cmd = [
        "keytool",
        "-list",
        "-v",
        "-keystore", KEYSTORE_PATH,
        "-storepass", KEYSTORE_PASSWORD
    ]
    
    try:
        result = subprocess.run(cmd, capture_output=True, text=True, check=False)
        
        if result.returncode == 0:
            print("[✓] Keystore verified successfully!")
            print(f"[*] Keystore size: {os.path.getsize(KEYSTORE_PATH)} bytes")
            return True
        else:
            print(f"[✗] Error verifying keystore:")
            print(f"STDERR: {result.stderr}")
            return False
            
    except Exception as e:
        print(f"[✗] Exception while verifying keystore: {e}")
        return False

def main():
    print("=" * 50)
    print("DeharPlayer Keystore Generator")
    print("=" * 50)
    print()
    
    if not generate_keystore():
        print("[✗] Failed to generate keystore")
        sys.exit(1)
    
    print()
    
    if not verify_keystore():
        print("[✗] Failed to verify keystore")
        sys.exit(1)
    
    print()
    print("[✓] Keystore setup completed successfully!")
    print(f"[*] Keystore path: {KEYSTORE_PATH}")
    print(f"[*] Alias: {KEY_ALIAS}")
    print()
    print("You can now build the APK with:")
    print("  ./gradlew.bat assembleRelease")
    

if __name__ == "__main__":
    main()
