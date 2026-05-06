document.addEventListener('DOMContentLoaded', function () {
    const updateModal = document.getElementById('updateProductModal');

    updateModal.addEventListener('show.bs.modal', function(event) {
        const button=event.relatedTarget;

        const image = button.getAttribute('data-bs-image');
        const id = button.getAttribute('data-bs-id');
        const name = button.getAttribute('data-bs-name');
        const price = button.getAttribute('data-bs-price');
        const stock = button.getAttribute('data-bs-stock');
        const productType = button.getAttribute('data-bs-productType');
        const active = button.getAttribute('data-bs-active');
        const imgElement = updateModal.querySelector('#img-preview');

        imgElement.src= image;

        updateModal.querySelector('#id').textContent = 'Product Id: '+ id;
        updateModal.querySelector('#product-id').value = id;
        updateModal.querySelector('#product-name').value = name;
        updateModal.querySelector('#product-price').value = price;
        updateModal.querySelector('#product-stock').value = stock;
        updateModal.querySelector('#product-category').value = productType;
        updateModal.querySelector('#product-active').value = active;
    });
});