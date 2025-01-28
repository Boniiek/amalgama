package org.example.services.optimization;

import org.example.data.Department;
import org.example.data.Factory;

public class OptimizationService {
    Factory factory;

    public OptimizationService(Factory factory) {
        this.factory = factory;
        if(factory!=null){
            if(factory.isValid()){
                //Распределить рабочих
//                factory.setWorkersCount(10);
                factory.divisionWorkers();
                factory.getFirstDepartment().setBufferCount(factory.getDetailsCount());
                start();
            }
        }
    }
    public void start(){
        int minutes=0;
        System.out.println("Time, ProductionCenter, WorkersCount, BufferCount");
        while(factory.isWork()){

            for(Department department: factory.getProductionCenters()){
                department.working(factory);
            }
            factory.print(minutes);
            minutes+=1;
        }
    }

}
