document.addEventListener('DOMContentLoaded', function () {
    const updateModal = document.getElementById('updateProductModal');

    updateModal.addEventListener('show.bs.modal', function(event) {
        const button=event.relatedTarget;

        const imageRoute = button.getAttribute('data-bs-image');
        const id = button.getAttribute('data-bs-id');
        const name = button.getAttribute('data-bs-name');
        const price = button.getAttribute('data-bs-price');
        const stock = button.getAttribute('data-bs-stock');
        const productType = button.getAttribute('data-bs-productType');
        const active = button.getAttribute('data-bs-active');

        updateModal.querySelector('#updateProductImage').value = imageRoute;
        updateModal.querySelector('#updateProductID').value = id;
        updateModal.querySelector('#updateProductName').value = name;
        updateModal.querySelector('#updateProductPrice').value = price;
        updateModal.querySelector('#updateProductStock').value = stock;
        updateModal.querySelector('#updateProductTypeId').value = productType;
        updateModal.querySelector('#updateProductIsActive').value = active;
    });
});