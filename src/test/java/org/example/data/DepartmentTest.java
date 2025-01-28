package org.example.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {
    Department department;
    @BeforeEach
    void dataConst(){
        department=new Department("Производственный центр №1",2.2f,2);
    }

    @Test
    void getWorkersCountTest() {
        assertEquals(0,department.getWorkersCount());
    }

    @Test
    void isValidTest() {
        assertFalse(department.isValid());
    }
    @Test
    void isValidTestWithDest() {
        Department departmentTest=new Department("Производственный центр №1",2.2f,2);
        department.getDestCenter().add(departmentTest);
        assertTrue(department.isValid());
    }

    @Test
    void addWorkers() {
        department.addWorkers();
        assertEquals(1,department.getWorkersCount());
    }

    @Test
    void acceptDetailFromSource() {
        department.acceptDetailFromSource(0.0f);
        assertEquals(1,department.getBufferCount());
    }
}