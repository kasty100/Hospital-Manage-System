package HospitalManagementSyatem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static  final String url="jdbc:mysql://127.0.0.1:3306/hospital";
    private static  final String username="root";

    private static  final String password="MySQL@&*(#";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            patient Patient=new patient(connection,scanner);
            doctor Doctor=new doctor(connection);
            while(true){
                System.out.println("Hospital Management System: " );
                System.out.println("1. Add Patients" );
                System.out.println("2. View Patients" );
                System.out.println("3. View Doctors" );
                System.out.println("4. Add Book Appointment" );
                System.out.println("5. Exit" );
                System.out.println("Enter your choice: " );
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        System.out.println(" ");
                        Patient.addPatient();
                        System.out.println(" ");
                        break;
                    case 2:
                        System.out.println(" ");
                        Patient.viewPatients();
                        System.out.println(" ");
                        break;
                    case 3:
                        System.out.println(" ");
                        Doctor.viewDoctors();
                        System.out.println(" ");
                        break;
                    case 4:
                        System.out.println(" ");
                        bookAppointment(Patient,Doctor,connection,scanner);
                        System.out.println(" ");
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Choice Invalid!!!");

                }


            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public  static  void    bookAppointment(patient Patient,doctor Doctor, Connection connection, Scanner scanner){
        System.out.println("Enter Patient Id: ");
        int patient_id= scanner.nextInt();
        System.out.println("Enter Doctor Id: ");
        int doctorId= scanner.nextInt();
        System.out.println("Enter Date (YYYY-MM-DD): ");
        String appointmnetDate=scanner.next();
        if(Patient.getPatientById(patient_id)&& Doctor.getDoctorById((doctorId))){
            if(checkDoctorAvailablity(doctorId,appointmnetDate,connection)){
                    String appointmentQuery="INSERT INTO appointments(p_id, d_id, appointment_date) VALUES(?, ?, ?)";
                    try{
                        PreparedStatement preparedStatement=connection.prepareStatement(appointmentQuery);
                        preparedStatement.setInt(1,patient_id);
                        preparedStatement.setInt(2,doctorId);
                        preparedStatement.setString(3,appointmnetDate);
                        int rowsAffected= preparedStatement.executeUpdate();
                        if(rowsAffected>0){
                            System.out.println("Appointment Booked");
                        }else{
                            System.out.println("Failed to Booked Appointment");

                        }

                    }catch(SQLException e){
                        e.printStackTrace();
                    }
            }else{
                System.out.println("Doctor not exits on same date, plz choose another date😓");
            }

        }else{
            System.out.println("Doctor or Patient dosen't exist");
        }



    }
    public static boolean checkDoctorAvailablity(int doctorId,String appointmnetDate, Connection connection){

                String query="select count(*) from appointments where d_id= ? and appointment_date = ?";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1,doctorId);
                    preparedStatement.setString(2,appointmnetDate);
                    ResultSet resultSet=preparedStatement.executeQuery();
                    if(resultSet.next()){
                        int count = resultSet.getInt(1);
                        if (count==0){
                            return true;
                        }else{
                            return  false;
                        }

                    }

                }catch (SQLException e){
                    e.printStackTrace();
                }
        return false;
    }




}
