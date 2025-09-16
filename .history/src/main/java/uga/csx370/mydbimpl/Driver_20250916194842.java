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
        Relation advisor = new RelationBuilder()
                .attributeNames(List.of("s_ID", "i_ID"))
                .attributeTypes(List.of(Type.STRING, Type.STRING))
                .build();
        advisor.loadData("src\\uni_in_class_exports\\advisor_export.csv");
        advisor.print();

        //Classroom Table

        Relation classroom = new RelationBuilder()
        .attributeNames(List.of("building", "room_number","capacity"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        classroom.loadData("src/uni_in_class_exports/classroom_export.csv");
        classroom.print();

        //Course Table


        //Dept Table


        //Instructor Table - priya
        Relation instructor = new RelationBuilder()
        .attributeNames(List.of("ID", "name","dept_name", "salary"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        classroom.loadData("src/uni_in_class_exports/instructor_export.csv");
        classroom.print();

        //Prereq Table - priya
        Relation prereq = new RelationBuilder()
        .attributeNames(List.of("course_id", "prereq_id"))
                .attributeTypes(List.of(Type.STRING, Type.STRING))
                .build();
        classroom.loadData("src/uni_in_class_exports/prereq_export.csv");
        classroom.print();

        //Section Table - priya
        Relation section = new RelationBuilder()
        .attributeNames(List.of("course_id", "sec_id","semester","year", "building","room_number", ))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        classroom.loadData("src/uni_in_class_exports/classroom_export.csv");
        classroom.print();

        //Student Table


        //Takes Table


        //Teaches Table


        //Time Slot Table

    }

}
