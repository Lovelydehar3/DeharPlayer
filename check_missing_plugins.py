import os

def check_files():
    missing_plugin_files = []
    for root, dirs, files in os.walk('.'):
        for file in files:
            if file == 'build.gradle.kts':
                path = os.path.join(root, file)
                try:
                    with open(path, 'r', encoding='utf-8') as f:
                        content = f.read()
                        if 'compose = true' in content:
                            if 'org.jetbrains.kotlin.plugin.compose' not in content and 'libs.plugins.kotlin.compose' not in content:
                                missing_plugin_files.append(path)
                except Exception as e:
                    print(f"Error reading {path}: {e}")
    
    for path in missing_plugin_files:
        print(path)

if __name__ == "__main__":
    check_files()
