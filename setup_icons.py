import os
import shutil
from PIL import Image

base_dir = "/home/darkwing/Desktop/nyay/NyayaSetu-Android-App/"
res_dir = os.path.join(base_dir, "app/src/main/res/")

# Create directories if they don't exist
os.makedirs(os.path.join(res_dir, "drawable"), exist_ok=True)
os.makedirs(os.path.join(res_dir, "mipmap-mdpi"), exist_ok=True)
os.makedirs(os.path.join(res_dir, "mipmap-hdpi"), exist_ok=True)
os.makedirs(os.path.join(res_dir, "mipmap-xhdpi"), exist_ok=True)
os.makedirs(os.path.join(res_dir, "mipmap-xxhdpi"), exist_ok=True)
os.makedirs(os.path.join(res_dir, "mipmap-xxxhdpi"), exist_ok=True)

# Step 3: Copy foreground and background
shutil.copy2(os.path.join(base_dir, "foreground.png"), os.path.join(res_dir, "drawable/ic_launcher_foreground.png"))
shutil.copy2(os.path.join(base_dir, "background.png"), os.path.join(res_dir, "drawable/ic_launcher_background.png"))

# Step 4: Scale legacy icons
sizes = {
    "mipmap-mdpi": (48, 48),
    "mipmap-hdpi": (72, 72),
    "mipmap-xhdpi": (96, 96),
    "mipmap-xxhdpi": (144, 144),
    "mipmap-xxxhdpi": (192, 192),
}

final_icon_path = os.path.join(base_dir, "final_icon.png")
with Image.open(final_icon_path) as img:
    for folder, size in sizes.items():
        resized = img.resize(size, Image.Resampling.LANCZOS)
        out_path = os.path.join(res_dir, folder, "ic_launcher.png")
        resized.save(out_path, "PNG")

print("Files created and copied successfully.")
