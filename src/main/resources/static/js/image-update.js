function previewImage(input) {
    const preview = document.getElementById('imagePreview');

    if (input.files && input.files[0]) {
        const reader= new FileReader();

        const imageUrl = URL.createObjectURL(input.files[0])

        preview.src = imageUrl;

        preview.onload = function () {
            URL.revokeObjectURL(preview.src);
        }
    }
}