document.addEventListener("DOMContentLoaded", () => {
    const canvas = document.getElementById("chart");

    if (canvas) { //Si existe canvas
        const ctx = canvas.getContext("2d");
        // 'reportData' ya existe porque lo definimos en el HTML
        console.log("ReportBody desde JS Externo:", reportData);
        console.log("Tipo de reporte:", reportType);
        //Convierte mi map en una lista del Dto
        const dataArray = Object.values(reportData)
        //Inicia mi muestra con tamaño original
        let top20Data = dataArray;
        //Recorta mi muestra a una longitud de max 20 objetos
        if (dataArray.length>20) top20Data=dataArray.slice(0,20)
        //Obtenemos los valores para las labels
        const labels = top20Data.map( item =>{
            console.log(item.name)
            return item.name || `Orden #${item.orderId}`
        })
        //Obtenemos los valores para las longitudes de barra
        const values = top20Data.map( item => {
            return item.total || item.quantitySold || item.totalOrders
        })
        //Generamos una etiqueta vacia
        let label="Report";
        if (reportType === 1) {
            label = "Date Range Report";
        }
        if (reportType === 2) {
            label = "Ranking Product Report";
        }
        if (reportType === 3) {
            label = "Ranking Customer Report";
        }


        new Chart(ctx,{
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: label,
                    data: values,
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        })
    }
});