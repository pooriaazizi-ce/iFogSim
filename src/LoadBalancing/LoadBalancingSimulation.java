package LoadBalancing;

// Application libraries
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LoadBalancingSimulation {


    public static void main(String[] str) {

    }


    /**
     * Creates a vanilla fog device
     * @param nodeName name of the device to be used in simulation
     * @param mips MIPS
     * @param ram RAM
     * @param upBw uplink bandwidth
     * @param downBw downlink bandwidth
     * @param level hierarchy level of the device
     * @param ratePerMips cost rate per MIPS used
     * @param busyPower
     * @param idlePower
     * @return
     */
    private static FogDevice createFogDevice(
            String nodeName, long mips, int ram, long upBw, long downBw,
            int level, double ratePerMips, double busyPower, double idlePower) {

        List<Pe> peList = new ArrayList<Pe>();

        // 3. Create PEs and add these into a list.
        // need to store Pe id and MIPS Rating
        peList.add(new Pe(0, new PeProvisionerOverbooking(mips)));

        int hostId = FogUtils.generateEntityId();

        // host storage
        long storage = 1000000;

        int bw = 10000;

        PowerHost host = new PowerHost(
                hostId,
                new RamProvisionerSimple(ram),
                new BwProvisionerOverbooking(bw),
                storage,
                peList,
                new StreamOperatorScheduler(peList),
                new FogLinearPowerModel(busyPower, idlePower)
        );

        List<Host> hostList = new ArrayList<Host>();
        hostList.add(host);

        // system architecture
        String arch = "x86";

        // operating system
        String os = "Linux";

        String vmm = "Xen";

        // time zone this resource located
        double time_zone = 10.0;

        // the cost of using processing in this resource
        double cost = 3.0;

        // the cost of using memory in this resource
        double costPerMem = 0.05;

        // the cost of using storage in this
        double costPerStorage = 0.001;

        // resource
        // the cost of using bw in this resource
        double costPerBw = 0.0;

        // we are not adding SAN
        LinkedList<Storage> storageList = new LinkedList<Storage>();

        // devices by now
        FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
                arch, os, vmm, host, time_zone, cost, costPerMem,
                costPerStorage, costPerBw);
        FogDevice fogdevice = null;

        try {
            fogdevice = new FogDevice(nodeName, characteristics,
                    new AppModuleAllocationPolicy(hostList), storageList, 10, upBw, downBw, 0, ratePerMips);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fogdevice.setLevel(level);
        return fogdevice;
    }

}




////import org.cloudbus.cloudsim.*;
////import org.cloudbus.cloudsim.core.CloudSim;
////import org.cloudbus.cloudsim.power.PowerHost;
////import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
////import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
////import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
////import org.fog.application.*;
////import org.fog.application.selectivity.FractionalSelectivity;
////import org.fog.entities.*;
////import org.fog.placement.Controller;
////import org.fog.placement.ModulePlacement;
////import org.fog.placement.ModulePlacementEdgewards;
////import org.fog.policy.AppModuleAllocationPolicy;
////import org.fog.scheduler.StreamOperatorScheduler;
////import org.fog.utils.*;
////import org.fog.utils.distribution.DeterministicDistribution;
////import java.util.*;
////
////public class LoadBalancingSimulation {
////
////    static List<FogDevice> fogDevices = new ArrayList<>();
////    static List<Sensor> sensors = new ArrayList<>();
////    static List<Actuator> actuators = new ArrayList<>();
////    static int userId;
////    static String appId = "HealthcareApp";
////
////    public static void main(String[] args) {
////
////        Log.printLine("Starting Load Balancing Simulation...");
////
////        try {
////            // Initialize CloudSim
////            CloudSim.init(1, Calendar.getInstance(), false);
////
////            // Create Fog devices and Cloud
////            FogDevice cloud = createFogDevice("Cloud", 10000, 10000, 10000, 10000, 0, 0.01, 200);
////            FogDevice mecServer1 = createFogDevice("MEC_Server1", 4000, 4000, 10000, 10000, 1, 0.1, 50);
////            FogDevice mecServer2 = createFogDevice("MEC_Server2", 4000, 4000, 10000, 10000, 1, 0.1, 50);
////            FogDevice mecServer3 = createFogDevice("MEC_Server3", 4000, 4000, 10000, 10000, 1, 0.1, 50);
////
////            // Add the Fog devices and Cloud to the environment
////            fogDevices.add(cloud);
////            fogDevices.add(mecServer1);
////            fogDevices.add(mecServer2);
////            fogDevices.add(mecServer3);
////
////            // Connect MEC servers to Cloud
////            mecServer1.setParentId(cloud.getId());
////            mecServer2.setParentId(cloud.getId());
////            mecServer3.setParentId(cloud.getId());
////
////            // Create IoT Devices (Sensors)
////            Sensor patientSensor1 = new Sensor("PatientSensor1", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////            Sensor patientSensor2 = new Sensor("PatientSensor2", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////            Sensor patientSensor3 = new Sensor("PatientSensor2", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////            Sensor patientSensor4 = new Sensor("PatientSensor2", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////            Sensor patientSensor5 = new Sensor("PatientSensor2", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////            Sensor patientSensor6 = new Sensor("PatientSensor2", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////            Sensor patientSensor7 = new Sensor("PatientSensor2", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////            Sensor patientSensor8 = new Sensor("PatientSensor2", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////            Sensor patientSensor9 = new Sensor("PatientSensor2", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////            Sensor patientSensor10 = new Sensor("PatientSensor2", "PATIENT_REQUEST", userId, appId, new DeterministicDistribution(5));
////
////            // Connect Sensors to MEC servers
////            patientSensor1.setGatewayDeviceId(mecServer1.getId());
////            // Latency between sensor and MEC server
////            patientSensor1.setLatency(2.0);
////
////            // Doing same thing for the other sensors
////            patientSensor2.setGatewayDeviceId(mecServer1.getId());
////            patientSensor2.setLatency(2.0);
////            patientSensor3.setGatewayDeviceId(mecServer1.getId());
////            patientSensor3.setLatency(2.0);
////            patientSensor4.setGatewayDeviceId(mecServer2.getId());
////            patientSensor4.setLatency(2.0);
////            patientSensor5.setGatewayDeviceId(mecServer2.getId());
////            patientSensor5.setLatency(2.0);
////            patientSensor6.setGatewayDeviceId(mecServer2.getId());
////            patientSensor6.setLatency(2.0);
////            patientSensor7.setGatewayDeviceId(mecServer3.getId());
////            patientSensor7.setLatency(2.0);
////            patientSensor8.setGatewayDeviceId(mecServer3.getId());
////            patientSensor8.setLatency(2.0);
////            patientSensor9.setGatewayDeviceId(mecServer3.getId());
////            patientSensor9.setLatency(2.0);
////            patientSensor10.setGatewayDeviceId(mecServer3.getId());
////            patientSensor10.setLatency(2.0);
////
////            sensors.add(patientSensor1);
////            sensors.add(patientSensor2);
////            sensors.add(patientSensor3);
////            sensors.add(patientSensor4);
////            sensors.add(patientSensor5);
////            sensors.add(patientSensor6);
////            sensors.add(patientSensor7);
////            sensors.add(patientSensor8);
////            sensors.add(patientSensor9);
////            sensors.add(patientSensor10);
////
////            // Create Application for patient data processing
////            Application app = createApplication();
////            app.setUserId(userId);
////
////            // Create a controller and submit the topology
////            Controller controller = new Controller("FogController", fogDevices, sensors, actuators);
////
////            // Define a scheduling algorithm (for load balancing)
////            controller.submitApplication(app, 0, new ModulePlacementEdgewards(fogDevices, sensors, app,));
////
////            // Start simulation
////            CloudSim.startSimulation();
////            CloudSim.stopSimulation();
////
////            Log.printLine("Simulation finished!");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////    /**
////     * Creates a Fog Device (MEC or Cloud)
////     */
////    public static FogDevice createFogDevice(String nodeName, long mips, int ram, long upBw, long downBw, int level, double ratePerMips, double latency) {
////        List<Pe> peList = new ArrayList<>();
////        peList.add(new Pe(0, new PeProvisionerOverbooking(mips)));  // Create processing element
////
////        int hostId = FogUtils.generateEntityId();
////        long storage = 1000000;  // Random storage value
////        long bw = 10000;
////
////        PowerHost host = new PowerHost(
////                hostId,
////                new RamProvisionerSimple(ram),
////                new BwProvisionerOverbooking(bw),
////                storage,
////                peList,
////                new StreamOperatorScheduler(peList),
////                new FogLinearPowerModel(87, 82)
////        );
////
////        List<Host> hostList = new ArrayList<>();
////        hostList.add(host);
////
////        FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
////                "x86", "Linux", "Xen", host, 10, 3, 0.05, 0.1, 0.1
////        );
////
////        FogDevice fogDevice = null;
////        try {
////            fogDevice = new FogDevice(nodeName, characteristics, new AppModuleAllocationPolicy(hostList), new LinkedList<Storage>(), 10, upBw, downBw, 0, ratePerMips);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        fogDevice.setLevel(level);
////        fogDevice.setUplinkLatency(latency);  // Latency to parent (higher-level) device
////        return fogDevice;
////    }
////
////    /**
////     * Creates an application for processing patient data
////     */
////    public static Application createApplication() {
////        Application application = new Application(appId, userId);
////
////        // Create application module for patient data processing
////        application.addAppModule("PatientProcessing", 10);  // Add application module
////
////        // Add edges to the application graph
////        application.addAppEdge("PATIENT_REQUEST", "PatientProcessing", 1000, 100, "PATIENT_REQUEST", Tuple.UP, AppEdge.SENSOR);  // From sensor to processing module
////        application.addAppEdge("PatientProcessing", "ACTUATOR", 1000, 100, "PATIENT_RESPONSE", Tuple.DOWN, AppEdge.ACTUATOR);  // From processing module to actuator
////
////        // Define module mapping
////        application.addTupleMapping("PatientProcessing", "PATIENT_REQUEST", "PATIENT_RESPONSE", new FractionalSelectivity(1.0));
////
////        return application;
////    }
////}
//
//import org.cloudbus.cloudsim.Host;
//import org.cloudbus.cloudsim.Log;
//import org.cloudbus.cloudsim.Pe;
//import org.cloudbus.cloudsim.Storage;
//import org.cloudbus.cloudsim.core.CloudSim;
//import org.cloudbus.cloudsim.power.PowerHost;
//import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
//import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
//import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
//import org.fog.application.AppEdge;
//import org.fog.application.Application;
//import org.fog.application.selectivity.FractionalSelectivity;
//import org.fog.entities.*;
//import org.fog.placement.Controller;
//import org.fog.placement.ModuleMapping;
//import org.fog.placement.ModulePlacementEdgewards;
//import org.fog.policy.AppModuleAllocationPolicy;
//import org.fog.scheduler.StreamOperatorScheduler;
//import org.fog.utils.FogLinearPowerModel;
//import org.fog.utils.FogUtils;
//import org.fog.utils.Logger;
//import org.fog.utils.distribution.DeterministicDistribution;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.LinkedList;
//import java.util.List;
//
//public class LoadBalancingSimulation {
//
//    static List<FogDevice> fogDevices = new ArrayList<>();
//    static List<Sensor> sensors = new ArrayList<>();
//    static List<Actuator> actuators = new ArrayList<>();
//    static int userId;
//    static String appId = "SimpleApp";  // Identifier for the application
//
//    public static void main(String[] args) {
//        Logger.ENABLED = true;
//        Log.printLine("Starting SimpleTopology Simulation...");
//
//        try {
//            // Initialize the CloudSim package
//            CloudSim.init(1, Calendar.getInstance(), false);
//
//            // Create Fog devices (MEC and Cloud)
//            FogDevice cloud = createFogDevice("Cloud", 10000, 10000, 10000, 10000, 0, 0.01, 200);
//            FogDevice mecServer = createFogDevice("MEC_Server", 4000, 4000, 10000, 10000, 1, 0.1, 50);
//
//            // Connect MEC server to Cloud
//            mecServer.setParentId(cloud.getId());
//
//            fogDevices.add(cloud);
//            fogDevices.add(mecServer);
//
//            // Create IoT Devices (Sensors)
//            Sensor sensor = new Sensor("PatientSensor", "PATIENT_DATA", userId, appId, new DeterministicDistribution(5));
//            sensors.add(sensor);
//
//            // Connect Sensor to MEC server
//            sensor.setGatewayDeviceId(mecServer.getId());
//            sensor.setLatency(2.0);  // Latency between sensor and MEC server
//
//            // Create an Application
//            Application app = createApplication();
//            app.setUserId(userId);
//
//            // Module Mapping: Specifies which fog device will run which module
//            ModuleMapping moduleMapping = ModuleMapping.createModuleMapping();
//
//            // Map the application module "DataProcessingModule" to MEC server
//            moduleMapping.addModuleToDevice("DataProcessingModule", "MEC_Server");
//
//            // Submit the application to the Controller
//            Controller controller = new Controller("FogController", fogDevices, sensors, actuators);
//            // controller.submitApplication(app, 0, new ModulePlacementEdgewards(fogDevices, sensors, app, moduleMapping));
//
//            // Submit the application with the corrected constructor
//            controller.submitApplication(app, 0, new ModulePlacementEdgewards(fogDevices, sensors, actuators, app, moduleMapping));
//
//
//            // Start the simulation
//            CloudSim.startSimulation();
//
//            // Stop the simulation
//            CloudSim.stopSimulation();
//
//            Log.printLine("SimpleTopology Simulation finished!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Creates a Fog Device (MEC or Cloud)
//     */
//    public static FogDevice createFogDevice(String nodeName, long mips, int ram, long upBw, long downBw, int level, double ratePerMips, double latency) {
//        List<Pe> peList = new ArrayList<>();
//        peList.add(new Pe(0, new PeProvisionerOverbooking(mips)));  // Create processing element
//
//        int hostId = FogUtils.generateEntityId();
//        long storage = 1000000;  // Random storage value
//        long bw = 10000;
//
//        PowerHost host = new PowerHost(
//                hostId,
//                new RamProvisionerSimple(ram),
//                new BwProvisionerOverbooking(bw),
//                storage,
//                peList,
//                new StreamOperatorScheduler(peList),
//                new FogLinearPowerModel(87, 82)
//        );
//
//        List<Host> hostList = new ArrayList<>();
//        hostList.add(host);
//
//        FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
//                "x86", "Linux", "Xen", host, 10, 3, 0.05, 0.1, 0.1
//        );
//
//        FogDevice fogDevice = null;
//        try {
//            fogDevice = new FogDevice(nodeName, characteristics, new AppModuleAllocationPolicy(hostList), new LinkedList<Storage>(), 10, upBw, downBw, 0, ratePerMips);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        fogDevice.setLevel(level);
//        fogDevice.setUplinkLatency(latency);  // Latency to parent (higher-level) device
//        return fogDevice;
//    }
//
//    /**
//     * Creates an application that processes data from a sensor
//     */
//    public static Application createApplication() {
//        Application application = new Application(appId, userId);
//
//        // Add an application module (e.g., data processing)
//        application.addAppModule("DataProcessingModule", 10);  // Add application module
//
//        // Add an application edge (e.g., data flow)
//        application.addAppEdge("PATIENT_DATA", "DataProcessingModule", 1000, 500, "PATIENT_DATA", Tuple.UP, AppEdge.SENSOR);
//        application.addAppEdge("DataProcessingModule", "PATIENT_RESPONSE", 1000, 500, "PATIENT_RESPONSE", Tuple.DOWN, AppEdge.ACTUATOR);
//
//        // Define how data moves between modules
//        application.addTupleMapping("DataProcessingModule", "PATIENT_DATA", "PATIENT_RESPONSE", new FractionalSelectivity(1.0));
//
//        return application;
//    }
//}
//



