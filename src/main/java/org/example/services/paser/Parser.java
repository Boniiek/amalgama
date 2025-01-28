package org.example.services.paser;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.data.Consts;
import org.example.data.Department;
import org.example.data.Factory;
import org.example.services.optimization.OptimizationService;
import org.example.services.paser.ConstsParsing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    private void parseTable(File table){

        XSSFWorkbook workbook=getBook(table);
        if(workbook!=null) {
            Factory factory =new Factory();
            parseWorkersCountAndDetailsCount(workbook,factory);
            parseProductionCenters(workbook,factory);
            parseConnection(workbook,factory);
            new OptimizationService(factory);
        }
    }
    private void parseConnection(Workbook workbook,Factory factory){
        Sheet sheet=workbook.getSheet(Consts.PAGE_NAME_CONNECTION);
        if(sheet!=null){
            List<String> listNameDepartment=factory.getProductionCenters().stream().map(Department::getNameDepartment).toList();
            int row= ConstsParsing.START_ROW_PARSE;
            int column=ConstsParsing.START_COLUMN_PARSE;
            if(sheet.getRow(row)!=null){
                while(sheet.getRow(row)!=null) {

                    Cell cellSource = sheet.getRow(row).getCell(column);
                    Cell cellDest=sheet.getRow(row).getCell(column+1);
                    if(cellDest!=null && cellDest.getCellType()==CellType.STRING && listNameDepartment.contains(cellDest.getStringCellValue()) &&
                            cellSource!=null && cellSource.getCellType()==CellType.STRING && listNameDepartment.contains(cellSource.getStringCellValue())
                    ){

                        factory.getDepartmentByName(cellSource.getStringCellValue()).getDestCenter().add(factory.getDepartmentByName(cellDest.getStringCellValue()));
                        factory.getDepartmentByName(cellDest.getStringCellValue()).getSourceCenter().add(factory.getDepartmentByName(cellSource.getStringCellValue()));
                    }else{
                        System.out.println("Неправильно обозначена связь");
                    }
                    row+=1;
                }

            }else{
                System.out.println("Информация не найдена");
            }
        }else{
            System.out.println("Страница : " + Consts.PAGE_NAME_CONNECTION + " не найдена");
        }
    }
    private void parseProductionCenters(Workbook workbook,Factory factory){
        List<Department> list=new ArrayList<>();
        Sheet sheet= workbook.getSheet(Consts.PAGE_NAME_PRODUCTION_CENTER);
        if(sheet!=null){
            int row=ConstsParsing.START_ROW_PARSE;
            Cell cell;
            //Парсинг Цехов
            while(sheet.getRow(row)!=null){
                String name;
                float performance;
                int maxWorkersCount;
                int column=ConstsParsing.START_COLUMN_PARSE_PRODUCTION_CENTER;
                //Парсинг имени Цеха
                cell=sheet.getRow(row).getCell(column);
                if(cell!=null && cell.getCellType()==CellType.STRING){
                    name= cell.getStringCellValue();
                    //Парсинг длительности обработки
                    column+=1;
                    cell=sheet.getRow(row).getCell(column);
                    if(cell!=null && cell.getCellType()==CellType.NUMERIC){
                        performance=(float)cell.getNumericCellValue();
                        column+=1;
                        cell=sheet.getRow(row).getCell(column);
                        if(cell!=null && cell.getCellType()==CellType.NUMERIC){
                            maxWorkersCount=(int) cell.getNumericCellValue();
                            list.add(new Department(name,performance,maxWorkersCount));
                        }else{
                            System.out.println("Не найдено количество рабочих");
                        }
                    }else{
                        System.out.println("Не найдено время обработки");
                    }
                }else{
                    System.out.println("Не найдено имя Цеха");
                }
                row+=1;
            }
        }else{
            System.out.println("Страница : " + Consts.PAGE_NAME_PRODUCTION_CENTER + " не найдена");
        }
        factory.setProductionCenters(list);
    }
    private void parseWorkersCountAndDetailsCount(Workbook workbook,Factory factory){
        Sheet sheet= workbook.getSheet(Consts.PAGE_NAME_SCENARIO);
        if(sheet!=null){
            int row =ConstsParsing.START_ROW_PARSE;
            int column=ConstsParsing.START_COLUMN_PARSE;
            if(sheet.getRow(row)!=null) {
                Cell cell = sheet.getRow(row).getCell(column);

                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    factory.setWorkersCount((int) cell.getNumericCellValue());
                    column += 1;
                    cell = sheet.getRow(row).getCell(column);
                    if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                        factory.setDetailsCount((int) cell.getNumericCellValue());
                    } else {
                        System.out.println("Информация о количестве деталях не найдена");
                    }
                } else {
                    System.out.println("Информация о количестве рабочих не найдена");
                }
            }else{
                System.out.println("Убедитесь что значения находятся на 3 строке в таблице ");
            }
        } else {
            System.out.println("Страница : " + Consts.PAGE_NAME_SCENARIO + " не найдена");
        }

    }
    private XSSFWorkbook getBook(File table){
        try{
            return (XSSFWorkbook) WorkbookFactory.create(table);
        }catch (Exception e){
            System.out.println("Проблема с открытием файла, убедитесь что он закрыт, и попробуйте снова");
        }
        return null;
    }
    public void choseCurrentTables(){
        File[] tables=findAllTables();
        if(tables!=null){
            System.out.println("Введите номер таблицы которую хотите проанализировать");

            boolean isChoose=false;
            while(!isChoose) {
                Scanner scanner =new Scanner(System.in);
                try {
                    int numberTable = scanner.nextInt();
                    if(numberTable<=tables.length && numberTable>=1) {
                        isChoose=true;
                        parseTable(tables[numberTable-1]);
                        scanner.close();
                    }else{
                        System.out.println("Таблицы с таким номером не существует");
                    }
                } catch (Exception InputMismatchException) {
                    System.out.println("Убедитесь что вы вводите только число");
                }
            }


        }

    }
    private File[] findAllTables(){
        File direction=new File(Consts.DIRECTION_TABLES);
        if(direction.exists()){
            File[] tables=direction.listFiles((dir,name)->name.endsWith(Consts.EXTENSION_TABLES));
            if(tables != null && tables.length>0){
                int numberTable=1;
                for(File table:tables){
                    System.out.println(numberTable+") "+table.getName());
                    numberTable+=1;
                }
                return tables;
            }else{
                System.out.println("Папка с таблицами пуста, пожалуйста добавьте нужные таблицы");
            }
        }else{
            System.out.println("Папка с таблицами не найдена. Пожалуйста убедитесь что она существует по пути : "+Consts.DIRECTION_TABLES);
        }
        return null;
    }
}
