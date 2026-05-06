document.addEventListener('DOMContentLoaded', () => {
    const input1 = document.querySelector('.input1');
    const input2 = document.querySelector('.input2');
    if (input2 && input2) {
        const form = input1.closest('form');
        const handleInput = () =>{
            const submitBtn = form.querySelector('.submitButton');
            const valuesMatch = (input1.value === input2.value) && input1.value !== "";
            submitBtn.disabled = !valuesMatch;
        }
        input1.addEventListener('input',handleInput);
        input2.addEventListener('input',handleInput);
    }
});