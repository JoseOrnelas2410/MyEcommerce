function changeQuantity(button,step) {
    const form = button.closest('form');
    const input = form.querySelector('.qty-input');
    const btnMinus = form.querySelector('.quantity-selector-minus');
    const btnPlus = form.querySelector('.quantity-selector-plus');
    const addToCart = form.querySelector('.button-add-to-cart');

    let currentQuantity = parseInt(input.value);
    const maxStock= parseInt(input.getAttribute('data-stock'))

    let newQuantity = currentQuantity +step;

    btnMinus.disabled = newQuantity === 0;
    addToCart.disabled = newQuantity === 0;
    btnPlus.disabled = newQuantity === maxStock;

    input.value = newQuantity;

}