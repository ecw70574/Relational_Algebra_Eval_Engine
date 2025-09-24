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
        System.out.println("This is the origional advisor Table");
        Relation advisor = new RelationBuilder()
                .attributeNames(List.of("s_ID", "i_ID"))
                .attributeTypes(List.of(Type.STRING, Type.STRING))
                .build();
        advisor.loadData("src/uni_in_class_exports/advisor_export.csv");

        //Classroom Table - mariah
        System.out.println("This is the origional classroom Table");
        Relation classroom = new RelationBuilder()
        .attributeNames(List.of("building", "room_number","capacity"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        classroom.loadData("src/uni_in_class_exports/classroom_export.csv");

        //Course Table - mariah 
        System.out.println("This is the origional course Table");
        Relation course = new RelationBuilder()
        .attributeNames(List.of("course_id", "title","dept_name","credits"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING,Type.DOUBLE))
                .build();
        course.loadData("src/uni_in_class_exports/course_export.csv");

        //Dept Table - mariah 
        System.out.println("This is the origional department Table");
        Relation dept = new RelationBuilder()
        .attributeNames(List.of("dept_name", "building","budget"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        dept.loadData("src/uni_in_class_exports/dept_export.csv");

        //Instructor Table - priya
        System.out.println("This is the origional Instructor Table");
        Relation instructor = new RelationBuilder()
        .attributeNames(List.of("ID", "name","dept_name", "salary"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        instructor.loadData("src/uni_in_class_exports/instructor_export.csv");

        //Prereq Table - priya
        System.out.println("This is the Original Prereq Table");
        Relation prereq = new RelationBuilder()
        .attributeNames(List.of("course_id", "prereq_id"))
                .attributeTypes(List.of(Type.STRING, Type.STRING))
                .build();
        prereq.loadData("src/uni_in_class_exports/prereq_export.csv");

        //Section Table - priya
        Relation section = new RelationBuilder()
                .attributeNames(List.of("course_id", "sec_id", "semester","year", "building", "room_number", "time_slot_id"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE, Type.STRING, Type.STRING, Type.STRING))
                .build();
        section.loadData("src/uni_in_class_exports/section_export.csv");
        
        //Student Table
        System.out.println("This is the Original Student Table");
        Relation student = new RelationBuilder().attributeNames(List.of("ID", "name","dept_name","tot_cred"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING,Type.DOUBLE))
                .build();
        student.loadData("src/uni_in_class_exports/student_export.csv");

        //Takes Table - Amy
        System.out.println("This is the Original Takes Table");
        Relation takes = new RelationBuilder()
                .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year", "grade"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE, Type.STRING))
                .build();
        takes.loadData("src/uni_in_class_exports/takes_export.csv");

        //Teaches Table - Amy
        System.out.println("This is the Original Teaches Table");
        Relation teaches = new RelationBuilder()
                .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        teaches.loadData("src/uni_in_class_exports/teaches_export.csv");

        //Time Slot Table - Amy
        System.out.println("This is the Original Time Slot Table");
        Relation Time_slot = new RelationBuilder()
                .attributeNames(List.of("time_slot_id", "day", "start_hr", "start_min", "end_hr", "end_min"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE, Type.DOUBLE, Type.DOUBLE, Type.DOUBLE))
                .build();
        Time_slot.loadData("src/uni_in_class_exports/time_slot_export.csv");


//Write Quereies Here


//Amy




//Rosie





//Priya



//Ella
// Names and IDs of instructors who advise John or Jack, & also taught in 2025
        RA ella_query = new RAImpl();
        // part 1 of query: get IDs of advisors for students named Jack or John
        Relation high_credits = ella_query.select(student, row -> {
                /*
                        String row_value = row.get(1).getAsString(); //row values for student name
                        return row_value.equals("Jack") || row_value.equals("John"); //equal "Jack or John"
                */ 
                double credits = row.get(3).getAsDouble();   // tot_cred
                return credits > 100;
                });
        high_credits.print();
        Relation stud_ids_only = ella_query.project(high_credits,List.of("ID"));
        Relation filtered_advisors = ella_query.join(stud_ids_only, advisor, row -> {
                int idx1 = stud_ids_only.getAttrIndex("ID");
                int idx2 = advisor.getAttrIndex("s_ID");
                return row.get(idx1).equals(row.get(idx2));
        });
        System.out.println("Advisors for students with 100+ credits");
        filtered_advisors.print();
        Relation advisor_ids = ella_query.project(filtered_advisors, List.of("i_ID"));
        Relation rename_highcred_advisors = ella_query.rename(advisor_ids, List.of("i_ID"), List.of("ID"));
        // now naming is consistent so joins/intersections are valid

        // part 2 of query: get IDs of instructors who taught in 2025
        Relation teach_2025 = ella_query.select(teaches, row -> {
                        double row_value_year = row.get(4).getAsDouble(); //row values for year
                        return row_value_year == 2024 || row_value_year.equals("2024"); //equal "2024"
                });
        Relation ids_2025 = ella_query.project(teach_2025, List.of("ID"));
        System.out.println("Professors who taught in 2024");
        ids_2025.print();
        
        Relation selected_instr_ids = ella_query.intersect(rename_highcred_advisors, ids_2025);

        Relation final_instr_info = ella_query.join(selected_instr_ids, instructor);
        Relation final_cols_only = ella_query.project(final_instr_info, List.of("ID", "name"));
        System.out.println("Names and IDs of instructors who advise John or Jack, & also taught in 2025");
        final_cols_only.print();
//Mariah 



/* 
        // Test Select Method: SELECT * FROM time_slot WHERE time_slot_id = "A";
        System.out.println("Select Test results: Select * from time_slot where time_slot_id = A");
        RAImpl test_time_slot_A = new RAImpl();
        Relation time_slot_A = test_time_slot_A.select(Time_slot, row -> {
                String row_value = row.get(0).getAsString(); //row values
                return row_value.equals("A"); //equal "A"
        });
        time_slot_A.print();

        // Test intersect method - Priya - using select tester
        Relation intersectionTest = test_time_slot_A.intersect(Time_slot, time_slot_A);

        System.out.println("Intersection results");
        intersectionTest.print();
        
        //Test Select Method: rename
        RAImpl test2 = new RAImpl();
        Relation renamed_Time_slot = test2.rename(
                Time_slot,
                List.of("day", "day"),
                List.of("weekday","hourssssss")
        );
        System.out.println("renamed relation");
        renamed_Time_slot.print();

	// Test project method - ella
	RAImpl test3 = new RAImpl();
	Relation project_og_timeslot = test3.project(Time_slot, List.of("day", "start_hr"));
        System.out.println("Testing project: project day and start_hr from og time_slot");
        project_og_timeslot.print();

	// Test union method - Ella
	// need to get another select output to union with time_slot_A
	RAImpl test4 = new RAImpl();
        Relation day_w = test4.select(Time_slot, row -> {
		String row_value = row.get(1).getAsString(); //row values
                return row_value.equals("W"); //equal "W"
        });
	System.out.println("Testing union method");
	Relation union_test = test4.union(time_slot_A, day_w);
	union_test.print();
	
        // Test diff method - Amy
        // Test 1: diff w/ itself should be empty
       System.out.println("Test diff method: 1) diff w/ itself should be empty");
        RAImpl test5 = new RAImpl();
        Relation diff_self_time_slot = test5.diff(Time_slot, Time_slot);
        diff_self_time_slot.print();
        // Test 2: diff w/ table (SELECT * FROM Time_slot WHERE time_slot_id != 'A';) should print only WHERE time_slot_id = 'A' rows
	System.out.println("Test diff method: 2) diff w/ non id A returns only id A's");
        RAImpl test6 = new RAImpl();
        System.out.println("- table w/ non A ids: ");
        Relation time_slot_not_A = test6.select(Time_slot, row -> {
                String row_value = row.get(0).getAsString(); //row values
                return !row_value.equals("A"); //equal "A"
        });
        time_slot_not_A.print();
        System.out.println("- set difference: ");
        Relation diff_A_time_slot = test6.diff(Time_slot, time_slot_not_A);
        diff_A_time_slot.print();

        //Test cartesian product method - Ella                                                                                        
        // case: with no column names in common                                                                                       

        RAImpl test5 = new RAImpl();
        // Incorporating student table that has no columns in common                                                                  
        Relation student = new RelationBuilder().attributeNames(List.of("ID", "name","dept_name","tot_cred"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING,Type.DOUBLE))
                .build();
        student.loadData("src/uni_in_class_exports/student_export.csv");
        // get only CS students                                                                                                       
        Relation cs_students = test5.select(student, row -> {
                String dept = row.get(2).getAsString();      // dept_name
                double credits = row.get(3).getAsDouble();   // tot_cred
                return dept.equals("Comp. Sci.") && credits > 100;
        });

        System.out.println("Computer Science students with 100+ credits only");
        cs_students.print();
        // cartesian product with only time slot A                                                                                    
        Relation cartesian_test = test5.cartesianProduct(time_slot_A, cs_students);
        System.out.println("Testing cartesian product method: ");
        cartesian_test.print();
        System.out.println("Length of Time slot A tbl: " + time_slot_A.getSize());
        System.out.println("Length of CS Students tbl: " + cs_students.getSize());
        System.out.println("Length of cartesian product of the two: " + cartesian_test.getSize());
        // need to test again with same col names to ensure exception is thrown

	// Test join method with department - ella
        Relation dept = new RelationBuilder()
        .attributeNames(List.of("dept_name", "building","budget"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        dept.loadData("src/uni_in_class_exports/dept_export.csv");

        System.out.println("Joining CS students with 100+ credits (student table) with department table");
        Relation join_test = test6.join(cs_students, dept);
        join_test.print();   

        System.out.println("This is the origional Instructor Table");
        Relation instructor1 = new RelationBuilder()
        .attributeNames(List.of("ID", "name","dept_name", "salary"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        instructor1.loadData("src/uni_in_class_exports/instructor_export.csv");
        instructor1.print();

        System.out.println("This is the origional department Table");
        Relation dept1 = new RelationBuilder()
        .attributeNames(List.of("dept_name", "building","budget"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        dept1.loadData("src/uni_in_class_exports/dept_export.csv");
        dept1.print();
        
        //Test natural join - Mariah 
        RA testJoin = new RAImpl();
        System.out.println("Joining instructor and department");
        Relation join_test2 = testJoin.join(instructor1, dept1);
        join_test2.print();


        //Section Table - priya
        System.out.println("This is the Original Section Table");
        Relation section = new RelationBuilder()
                .attributeNames(List.of("course_id", "sec_id", "semester","year", "building", "room_number", "time_slot_id"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE, Type.STRING, Type.STRING, Type.STRING))
                .build();
        section.loadData("src/uni_in_class_exports/section_export.csv");

        System.out.println("This is the Original Teaches Table");
        Relation teaches = new RelationBuilder()
                .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        teaches.loadData("src/uni_in_class_exports/teaches_export.csv");

        //Test natural join 2 - Mariah 
        RA testJoin2 = new RAImpl();
        System.out.println("Joining teaches and sector");
        Relation join_test3 = testJoin2.join(teaches, section);
        join_test3.print();
*/




        
    }



}
