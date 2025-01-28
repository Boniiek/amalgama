package org.example.data;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class Department {
    private String nameDepartment;
    private float performance;
    private int maxWorkersCount;
    private int bufferCount;
    private int workersCount;
    private List<Float> listDetailInWork;
    private List<Department> destCenter;
    private List<Department> sourceCenter;
    private int nextDest=0;
    public Department(String nameDepartment,float performance, int maxWorkersCount ) {
        this.nameDepartment = nameDepartment;
        this.maxWorkersCount = maxWorkersCount;
        this.performance = performance;
        this.destCenter=new ArrayList<>();
        this.sourceCenter= new ArrayList<>();
        this.listDetailInWork=new ArrayList<>();
        this.workersCount=0;
    }
    public int getBufferCount() {
        return bufferCount;
    }

    public List<Float> getListDetailInWork() {
        return listDetailInWork;
    }

    public void setBufferCount(int bufferCount) {
        this.bufferCount = bufferCount;
    }
    public int getWorkersCount() {
        return workersCount;
    }
    public void setWorkersCount(int workersCount) {
        this.workersCount = workersCount;
    }
    @Override
    public String toString() {
        return "Department{" +
                "nameDepartment='" + nameDepartment + '\'' +
                ", performance=" + performance +
                ", maxWorkersCount=" + maxWorkersCount +
                '}';
    }
    public String getNameDepartment() {
        return nameDepartment;
    }
    public int getMaxWorkersCount() {
        return maxWorkersCount;
    }
    public List<Department> getSourceCenter() {
        return sourceCenter;
    }
    public List<Department> getDestCenter() {
        return destCenter;
    }
    //Проверка на валидность
    public boolean isValid(){
        return (!nameDepartment.isEmpty() && performance>0 && maxWorkersCount>0 && (!destCenter.isEmpty() || !sourceCenter.isEmpty() ));
    }
    public void addWorkers(){
        this.workersCount+=1;
    }
    public void working(Factory factory){
        if(!listDetailInWork.isEmpty()){
            ListIterator<Float> iterator=listDetailInWork.listIterator();
            while (iterator.hasNext()){
                float num= iterator.next();
                num+=1;
                iterator.remove();
                if(num>performance){
                    if(!destCenter.isEmpty()) {
                    //Перевод в следующий цех проверить естьт ли там рабочие и есть ли там Buffercount
                    destCenter.get(nextDest).acceptDetailFromSource((num-performance)%1);
                    //Итератор пооочередно отправляем детали в разные цеха
                    nextDest = (nextDest + 1) % destCenter.size();
                    }
                    if(bufferCount!=0){
                        iterator.add((num-performance)%1);
                        bufferCount-=1;
                    }else if (workersCount>=1){
                        for(Department department: factory.getProductionCenters()){
                            if(department.getBufferCount()>0 && department.getListDetailInWork().size()<department.getMaxWorkersCount() && department.getMaxWorkersCount()>department.getWorkersCount()){
                                department.addWorkers();
                                department.listDetailInWork.add((num-performance)%1);
                                this.workersCount-=1;
                                break;
                            }
                        }
                    }
                }else{
                    iterator.add(num);
                }
            }

        }
        if(listDetailInWork.isEmpty() && bufferCount!=0 && workersCount>0){
            int bufferWorkers=workersCount;
            while(bufferWorkers>0 && bufferCount!=0){
                listDetailInWork.add(0f);
                bufferWorkers-=1;
                bufferCount-=1;
            }
        }
    }
    public void acceptDetailFromSource(float timer){
        //Проверяем есть ли свободные рабочие
        if(listDetailInWork.size()==workersCount){
            bufferCount+=1;
        }else if(listDetailInWork.size()<=workersCount){
            listDetailInWork.add(timer-1f);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Float.compare(performance, that.performance) == 0 && maxWorkersCount == that.maxWorkersCount && bufferCount == that.bufferCount && workersCount == that.workersCount && Objects.equals(nameDepartment, that.nameDepartment) && Objects.equals(destCenter, that.destCenter) && Objects.equals(sourceCenter, that.sourceCenter);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nameDepartment, performance, maxWorkersCount, bufferCount, workersCount, destCenter, sourceCenter);
    }
}
