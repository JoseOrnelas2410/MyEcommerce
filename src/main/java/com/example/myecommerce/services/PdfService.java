package com.example.myecommerce.services;

import com.example.myecommerce.models.dto.DateRangeReportDto;
import com.example.myecommerce.models.dto.RankingCustomerDto;
import com.example.myecommerce.models.dto.RankingProductDto;
import com.example.myecommerce.models.entity.Order;
import com.example.myecommerce.models.entity.OrderFraction;
import com.example.myecommerce.util.ReportUtil;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openpdf.text.Document;
import org.openpdf.text.Image;
import org.openpdf.text.PageSize;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class PdfService {

    private final OrderService orderService;

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void generate(ServletOutputStream outputStream,
                         int reportType,
                         Date from,
                         Date to) throws IOException {
        List<Order> orders = orderService.getOrdersByDateRange(from,to);

        String title = switch (reportType) {
            case 1 -> "Date Range Report";
            case 2 -> "Ranking Product Report";
            case 3 -> "Ranking Customer Report";
            default ->throw new IllegalArgumentException("Report type unavalible");
        };


        /**
         * Chart
         */


        /**
         * Llenado de tabla
         * */

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        PdfPTable header = new PdfPTable(1);
        header.addCell("MyECommerce");
        header.addCell(title);
        PdfPTable contentTable;
        JFreeChart barChart;
        DefaultCategoryDataset chartData = new DefaultCategoryDataset();//ChartData

         switch (reportType) {
             case 1:
                 List<DateRangeReportDto> dateRangeList = ReportUtil.getDateRangeList(orders);
                 contentTable = dateRangeReport(dateRangeList);
                 if (dateRangeList.size()>10) dateRangeList = dateRangeList.subList(0,9);
                 dateRangeList.forEach(item-> {
                     chartData.addValue(
                             item.getTotal(),
                             "Date Range Report",
                             item.getOrderId()
                     );
                 });
                 barChart = ChartFactory.createBarChart(
                         "Top Orders",
                         "Order Id",
                         "Total Value",
                         chartData,
                         PlotOrientation.HORIZONTAL,
                         false,
                         false,
                         false);
                break;
             case 2:
                 List<RankingProductDto> rankingProductList = ReportUtil.getRankingProductList(orders);
                 contentTable = rankingProductReport(rankingProductList);
                 if (rankingProductList.size()>10) rankingProductList = rankingProductList.subList(0,9);
                 rankingProductList.forEach(item-> {
                     chartData.addValue(
                             item.getQuantitySold(),
                             "Top Ranking Products",
                             item.getName()
                     );
                 });
                 barChart = ChartFactory.createBarChart(
                         "Top Products",
                         "Products",
                         "Quantity Sold",
                         chartData,
                         PlotOrientation.VERTICAL,
                         false,
                         false,
                         false);
                break;
             case 3:
                 List<RankingCustomerDto> rankingCustomerList = ReportUtil.getRankingCustomerList(orders);
                 contentTable = customerReport(rankingCustomerList);
                 if (rankingCustomerList.size()>10) rankingCustomerList = rankingCustomerList.subList(0,9);
                 rankingCustomerList.forEach(item->{
                     chartData.addValue(
                             item.getTotalOrders(),
                             "Top Ranking Customers",
                             item.getName()
                     );
                 });
                 barChart = ChartFactory.createBarChart(
                         "Top Customers",
                         "Customers",
                         "Number of Orders",
                         chartData,
                         PlotOrientation.HORIZONTAL,
                         false,
                         false,
                         false);
                break;
             default:
                 throw new IllegalArgumentException("Report type unavalible");
        }

        BufferedImage bufferedImage = barChart.createBufferedImage(500, 300);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,"png", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        Image chartImage = Image.getInstance(imageBytes);

        /*Inicia escritura de documento*/
        document.open();

        document.add(header);
        document.add(chartImage);
        document.add(contentTable);
        document.addCreationDate();
        document.close();

    }

    private PdfPTable dateRangeReport(List<DateRangeReportDto> dateRangeList){
        AtomicInteger rank = new AtomicInteger(1);
        PdfPTable content = new PdfPTable(5);
        content.addCell("Position");
        content.addCell("Order Id");
        content.addCell("Customer");
        content.addCell("Total");
        content.addCell("Order Date");
        dateRangeList.forEach(item-> {
            content.addCell(String.valueOf(rank.getAndIncrement()));
            content.addCell(String.valueOf(item.getOrderId()));
            content.addCell(item.getCustomerName());
            content.addCell(String.valueOf(item.getTotal()));
            content.addCell(String.valueOf(item.getOrderDate()));
        });
        return content;
    }

    private PdfPTable rankingProductReport(List<RankingProductDto> rankingProductList) {
        AtomicInteger rank = new AtomicInteger(1);
        PdfPTable content = new PdfPTable(6);
        content.addCell("Position");
        content.addCell("Product Id");
        content.addCell("Name");
        content.addCell("Quantity Sold");
        content.addCell("ActualStock");
        content.addCell("Category");
        rankingProductList.forEach(item -> {
            System.out.println(item.toString());
            content.addCell(String.valueOf(rank.getAndIncrement()));
            content.addCell(String.valueOf(item.getProductId()));
            content.addCell(item.getName());
            content.addCell(String.valueOf(item.getQuantitySold()));
            content.addCell(String.valueOf(item.getActualStock()));
            content.addCell(item.getCategory());
        });
        return content;
    }

    private PdfPTable customerReport(List<RankingCustomerDto> rankingCustomerList){
        AtomicInteger rank = new AtomicInteger(1);
        PdfPTable content = new PdfPTable(5);
        content.addCell("Position");
        content.addCell("Customer");
        content.addCell("Total Orders");
        content.addCell("Total Payed");
        content.addCell("Last Order");
        rankingCustomerList.forEach(item -> {
            System.out.println(item.toString());
            content.addCell(String.valueOf(rank.getAndIncrement()));
            content.addCell(item.getName());
            content.addCell(String.valueOf(item.getTotalOrders()));
            content.addCell(String.valueOf(item.getTotalPayed()));
            content.addCell(String.valueOf(item.getLastOrderDate()));
        });
        return content;
    }



}
