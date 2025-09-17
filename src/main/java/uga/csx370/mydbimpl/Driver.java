/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package uga.csx370.mydbimpl;

import java.util.List;

import uga.csx370.mydb.Relation;
import uga.csx370.mydb.RelationBuilder;
import uga.csx370.mydb.Type;

public class Driver {
    
    public static void main(String[] args) {
        // Following is an example of how to use the relation class.
        // This creates a table with three columns with below mentioned
        // column names and data types.
        // After creating the table, data is loaded from a CSV file.
        // Path should be replaced with a correct file path for a compatible
        // CSV file.

        //Advisor Table
/*         System.out.println("This is the origional advisor Table");
        Relation advisor = new RelationBuilder()
                .attributeNames(List.of("s_ID", "i_ID"))
                .attributeTypes(List.of(Type.STRING, Type.STRING))
                .build();
        advisor.loadData("src/uni_in_class_exports/advisor_export.csv");
        advisor.print();

        //Classroom Table - mariah
        System.out.println("This is the origional classroom Table");
        Relation classroom = new RelationBuilder()
        .attributeNames(List.of("building", "room_number","capacity"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        classroom.loadData("src/uni_in_class_exports/classroom_export.csv");
        classroom.print();

        //Course Table - mariah 
        System.out.println("This is the origional course Table");
        Relation course = new RelationBuilder()
        .attributeNames(List.of("course_id", "title","dept_name","credits"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING,Type.DOUBLE))
                .build();
        course.loadData("src/uni_in_class_exports/course_export.csv");
        course.print();

        //Dept Table - mariah 
        System.out.println("This is the origional deptartment Table");
        Relation dept = new RelationBuilder()
        .attributeNames(List.of("dept_name", "building","budget"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        dept.loadData("src/uni_in_class_exports/dept_export.csv");
        dept.print();

        //Instructor Table - priya
        System.out.println("This is the origional Instructor Table");
        Relation instructor = new RelationBuilder()
        .attributeNames(List.of("ID", "name","dept_name", "salary"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        instructor.loadData("src/uni_in_class_exports/instructor_export.csv");
        instructor.print();

        //Prereq Table - priya
        System.out.println("This is the Original Prereq Table");
        Relation prereq = new RelationBuilder()
        .attributeNames(List.of("course_id", "prereq_id"))
                .attributeTypes(List.of(Type.STRING, Type.STRING))
                .build();
        prereq.loadData("src/uni_in_class_exports/prereq_export.csv");
        prereq.print();
*/
        //Section Table - priya
/*        System.out.println("This is the Original Section Table");
        Relation section = new RelationBuilder()
        .attributeNames(List.of("sec_id", "semester","year", "building", "room_number", "time_slot_id"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE, Type.STRING, Type.STRING, Type.STRING))
                .build();
        section.loadData("src/uni_in_class_exports/section_export.csv");
        section.print();
*/        
        //Student Table
/*        System.out.println("This is the Original Student Table");
        Relation student = new RelationBuilder().attributeNames(List.of("ID", "name","dept_name","tot_cred"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING,Type.DOUBLE))
                .build();
        student.loadData("src/uni_in_class_exports/student_export.csv");
        student.print();

        //Takes Table - Amy
        System.out.println("This is the Original Takes Table");
        Relation takes = new RelationBuilder()
                .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year", "grade"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE, Type.STRING))
                .build();
        takes.loadData("src/uni_in_class_exports/takes_export.csv");
        takes.print();

        //Teaches Table - Amy
        System.out.println("This is the Original Teaches Table");
        Relation teaches = new RelationBuilder()
                .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        teaches.loadData("src/uni_in_class_exports/teaches_export.csv");
        teaches.print();
*/
        //Time Slot Table - Amy
        System.out.println("This is the Original Time Slot Table");
        Relation Time_slot = new RelationBuilder()
                .attributeNames(List.of("time_slot_id", "day", "start_hr", "start_min", "end_hr", "end_min"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE, Type.DOUBLE, Type.DOUBLE, Type.DOUBLE))
                .build();
        Time_slot.loadData("src/uni_in_class_exports/time_slot_export.csv");
        Time_slot.print();

        // Test Select Method: select(Relation rel, Predicate p)
        RAImpl test_time_slot_A = new RAImpl();
        Relation time_slot_A = test_time_slot_A.select(Time_slot, row -> row.get(0).getAsString().equals("A")); //List<Cell> row
        time_slot_A.print();
    }

}
