import json
import re
import sys

# Load the version check results
with open('versions.json', 'r') as f:
    results = json.load(f)

updated_files = []
updates = []

for result in results:
    # Skip if already latest or has errors
    if result.get('is_latest') or result.get('error'):
        continue

    newer_versions = result.get('newer_versions', [])
    if not newer_versions:
        continue

    image = result['image']
    file_path = image['file_path']
    property_name = image['property_name']
    current_version = image['current_version']
    latest_version = result['latest_version']

    # Read the properties file
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Build the full image reference for find/replace
        registry = image['registry']
        namespace = image['namespace']
        name = image['name']

        # Construct the image path
        if namespace:
            image_path = f"{registry}/{namespace}/{name}" if registry not in ['docker.io', ''] else f"{namespace}/{name}"
        else:
            image_path = f"{registry}/{name}" if registry not in ['docker.io', ''] else name

        # Create the old and new references
        old_ref = f"{image_path}:{current_version}"
        new_ref = f"{image_path}:{latest_version}"

        # Also handle case where registry might be implicit
        if registry in ['docker.io', '']:
            # Try both with and without explicit registry
            old_pattern = f"(docker\\.io/)?{re.escape(image_path)}:{re.escape(current_version)}"
            new_content = re.sub(old_pattern, new_ref, content)
        else:
            # Direct replacement
            new_content = content.replace(old_ref, new_ref)

        if new_content != content:
            # Write the updated content
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(new_content)

            updated_files.append(file_path)
            updates.append({
                'property': property_name,
                'file': file_path,
                'old_version': current_version,
                'new_version': latest_version,
                'image_name': image_path
            })

            print(f"✅ Updated {property_name}: {current_version} → {latest_version}")
        else:
            print(f"⚠️  Could not update {property_name} in {file_path}")

    except Exception as e:
        print(f"❌ Error updating {file_path}: {e}")
        continue

# Save the updates for PR body
with open('updates.json', 'w') as f:
    json.dump(updates, f, indent=2)

print(f"\nTotal files updated: {len(updated_files)}")
sys.exit(0 if updated_files else 1)
