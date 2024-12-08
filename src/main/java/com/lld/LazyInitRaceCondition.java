package com.lld;

public class LazyInitRaceCondition {

}

//
//public class DBConnection {
//    public static PostgresConnection instance;
//
//    public DBConnection() {}
//
////    singleton pattern
//    public PostgresConnection getInstance() {
//        synchronized (this) {
//            if (instance == null) {
//                    instance = new PostgresConnection();
//            }
//        }
//        return instance;
//    }
//}
//
//class Runner {
//    public static void main(String[] args) {
//        DBConnection conn1 = new DBConnection();
//        DBConnection conn2 = new DBConnection();
//        System.out.println(conn1.getInstance());
//        System.out.println(conn2.getInstance());
//    }
//}
