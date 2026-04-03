import sys
from PIL import Image, ImageEnhance, ImageFilter
import numpy as np

try:
    img_path = "/home/darkwing/.gemini/antigravity/brain/e01775ef-3b62-45a1-9240-b9d4ef34ed64/media__1775195660078.jpg"
    img = Image.open(img_path).convert("RGBA")
    arr = np.array(img)
    
    # 1. Background Extraction
    # Top-center color and bottom-center color to build a pure background
    top_color = arr[int(arr.shape[0]*0.15), int(arr.shape[1]*0.5), :3]
    bottom_color = arr[int(arr.shape[0]*0.85), int(arr.shape[1]*0.5), :3]
    
    # Simple color distance for masking
    # Gold vs Blue. Blue has B > R. Gold has R > B.
    # Let's use red dominance and overall brightness
    r_ch = arr[:, :, 0].astype(int)
    g_ch = arr[:, :, 1].astype(int)
    b_ch = arr[:, :, 2].astype(int)
    
    # Mask where Red > Blue + 20 and Green > Blue + 20 (Typical for Gold/Yellow)
    mask = (r_ch > b_ch + 10) & (g_ch > b_ch + 10)
    
    y_indices, x_indices = np.where(mask)
    if len(y_indices) > 0:
        min_y, max_y = y_indices.min(), y_indices.max()
        min_x, max_x = x_indices.min(), x_indices.max()
    else:
        # Fallback if color space is weird
        min_y, max_y, min_x, max_x = 0, arr.shape[0]-1, 0, arr.shape[1]-1
        
    scale_arr = arr[min_y:max_y+1, min_x:max_x+1].copy()
    scale_mask = mask[min_y:max_y+1, min_x:max_x+1]
    
    # Convert binary mask to soft alpha image for blending
    mask_img = Image.fromarray(np.uint8(scale_mask * 255), "L")
    mask_img = mask_img.filter(ImageFilter.BoxBlur(1)) # Soften edges
    
    # Create RGBA foreground
    scale_img = Image.fromarray(scale_arr)
    scale_img.putalpha(mask_img)
    
    # Proper padding (symbol is ~65% of 1024 -> ~665px)
    target_symbol_size = 665
    scale_aspect = scale_img.width / scale_img.height
    if scale_aspect > 1:
        new_w = target_symbol_size
        new_h = int(target_symbol_size / scale_aspect)
    else:
        new_h = target_symbol_size
        new_w = int(target_symbol_size * scale_aspect)
        
    foreground_img = scale_img.resize((new_w, new_h), Image.Resampling.LANCZOS)
    
    # Enhance slightly (sharpen)
    enhancer = ImageEnhance.Sharpness(foreground_img)
    foreground_img = enhancer.enhance(1.8)
    
    fg_final = Image.new("RGBA", (1024, 1024), (0, 0, 0, 0))
    paste_x = (1024 - new_w) // 2
    paste_y = (1024 - new_h) // 2
    fg_final.paste(foreground_img, (paste_x, paste_y))
    fg_final.save("/home/darkwing/Desktop/nyay/NyayaSetu-Android-App/foreground.png", "PNG")
    
    # Background generation (Gradient from top_color to bottom_color)
    bg_final_arr = np.zeros((1024, 1024, 4), dtype=np.uint8)
    for i in range(1024):
        ratio = i / 1023.0
        r = int(top_color[0] * (1 - ratio) + bottom_color[0] * ratio)
        g = int(top_color[1] * (1 - ratio) + bottom_color[1] * ratio)
        b = int(top_color[2] * (1 - ratio) + bottom_color[2] * ratio)
        bg_final_arr[i, :, :] = [r, g, b, 255]
        
    bg_final = Image.fromarray(bg_final_arr)
    bg_final.save("/home/darkwing/Desktop/nyay/NyayaSetu-Android-App/background.png", "PNG")
    
    # Combined final icon
    final_icon = bg_final.copy()
    final_icon.paste(fg_final, (0, 0), fg_final)
    final_icon.save("/home/darkwing/Desktop/nyay/NyayaSetu-Android-App/final_icon.png", "PNG")
    
    print("SUCCESS")
except Exception as e:
    print(f"FAILED: {e}")
