document.querySelectorAll('.image-input').forEach( input => {
    input.addEventListener("change", function () {
        const container= this.closest('.form-img-container');
        const preview = container.querySelector('.img-preview');

        if (this.files && this.files[0]) {//Verificamos archivo seleccionado
            const file = this.files[0];

            const imageUrl = URL.createObjectURL(file);

            preview.src = imageUrl;

            preview.onload = function () {
                URL.revokeObjectURL(imageUrl)
            }

            console.log("Blob generado para el input", this.id, imageUrl);
        }

    });
});