document.addEventListener('DOMContentLoaded', function () {
   const updateCategoryModal = document.getElementById('updateCategoryModal');

   updateCategoryModal.addEventListener('show.bs.modal', function (event) {
       const button=event.relatedTarget;
       const value = button.getAttribute('data-bs-value');
       const description = button.getAttribute('data-bs-description');

       updateCategoryModal.querySelector('#id').textContent = 'Category Id ' + value;
       updateCategoryModal.querySelector('#category-value').value = value;
       updateCategoryModal.querySelector('#category-description').value = description;
   });
});