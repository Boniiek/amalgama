package org.example.data;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class Factory {
    private int workersCount;
    private int detailsCount;
    private List<Department> productionCenters;
    private Department firstDepartment;

    public Factory(){
        productionCenters=new ArrayList<>();
    }
    public Department getFirstDepartment() {
        return firstDepartment;
    }
    public void setWorkersCount(int workersCount) {
        this.workersCount = workersCount;
    }

    public int getDetailsCount() {
        return detailsCount;
    }

    public void setDetailsCount(int detailsCount) {
        this.detailsCount = detailsCount;
    }

    public List<Department> getProductionCenters() {
        return productionCenters;
    }

    public void setProductionCenters(List<Department> productionCenters) {
        this.productionCenters = productionCenters;
    }

    @Override
    public String toString() {
        return "Factory{" +
                "workersCount=" + workersCount +
                ", detailsCount=" + detailsCount +
                ", productionCenters=" + productionCenters +
                '}';
    }
    public Department getDepartmentByName(String name){
        for(Department department:productionCenters){
            if(department.getNameDepartment().equals(name))return department;
        }
        return null;
    }
    public boolean isValid(){
        return workersCount>0 && detailsCount>0 && !productionCenters.isEmpty() && isValidProductionCenters()
                && findFirst() && findLast();
    }
    private boolean isValidProductionCenters(){
        if(!productionCenters.isEmpty()) {
            for (Department department : productionCenters) {
                if (!department.isValid()) {
                    System.out.println("У " + department.getNameDepartment() + " имеются пустые поля");
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }
    private boolean findFirst(){
        int count=0;
        if(!productionCenters.isEmpty()){

            for(Department department:productionCenters){
                if(department.getSourceCenter().isEmpty()){
                    System.out.println("Первый цех :"+ department.getNameDepartment());
                    firstDepartment=department;
                    count+=1;
                }
            }
        }
        return count==1;
    }
    private boolean findLast(){
        int count=0;
        if(!productionCenters.isEmpty()){

            for(Department department:productionCenters){
                if(department.getDestCenter().isEmpty()){
                    System.out.println("Последний цех :"+ department.getNameDepartment());
                    count+=1;
                }
            }
        }
        return count==1;
    }
    public void divisionWorkers(){
        int workersCount=this.workersCount;
        if(workersCount>=this.firstDepartment.getMaxWorkersCount()){
            this.firstDepartment.setWorkersCount(this.firstDepartment.getMaxWorkersCount());
            workersCount-=this.firstDepartment.getMaxWorkersCount();
            List<Department> listNextDepartments= new ArrayList<>(firstDepartment.getDestCenter()) ;
            boolean isRemainDepartment=true;
            while(workersCount>0 &&isRemainDepartment){
                int workersCountNextDepartments=0;
                if(!listNextDepartments.isEmpty()){
                    //Высчитываем сколько нужно чтобы заполнитьследющую ступень сотрудниками
                    for(Department department:listNextDepartments){
                        workersCountNextDepartments=workersCountNextDepartments+department.getMaxWorkersCount()-department.getWorkersCount();
                    }
                    if(workersCount>=workersCountNextDepartments){
                        workersCount-=workersCountNextDepartments;
                        //Переделать список
                        List<Department> listNextDepartmentsBuffer=new ArrayList<>(listNextDepartments);
                        listNextDepartments.clear();
                        for(Department department:listNextDepartmentsBuffer){
                            department.setWorkersCount(department.getMaxWorkersCount());
                            listNextDepartments= ListUtils.union(listNextDepartments,department.getDestCenter());
                        }
                    }
                    //Распределение если рабочих меньше чем максимальное возможное на следующей ступени
                    else{
                        while(workersCount>0){
                            for(Department department:listNextDepartments){
                                if(department.getWorkersCount()<department.getMaxWorkersCount()&&workersCount>0){
                                    department.addWorkers();
                                    workersCount-=1;
                                }
                            }
                        }
                    }
                }else{
                    isRemainDepartment=false;
                }
            }
        }
    }
    public void print(float time){
        for(Department department:productionCenters){
            System.out.println(time+", "+
                    department.getNameDepartment()+", "+
                    department.getWorkersCount()+", "+
                    department.getBufferCount()
                    );
        }
    }
    public boolean isWork(){
        int sum=0;
        for(Department department:productionCenters){
            sum+= department.getBufferCount();
            if(!department.getListDetailInWork().isEmpty()){
                sum+=department.getListDetailInWork().size();
            }
        }
        return sum>0;
    }
}
