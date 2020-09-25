package alarm;


public class ALARM_FunctionalMain {

    protected ALARM_Functional alarm = new ALARM_Functional();


    public void DoSimSymbolic() {
        this.alarm.ALARM_FunctionalSymWrapper(1, 1, false, 1, 1, false, false, 1, false, 1,
                false, false, false, false, false, false, false, false, false, false,
                false, 1, false, 1, false, false, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, false, false, false,
                false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, false, 1, 1, 1, false, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, false, false, false, false, 1,
                1, 1, 1, 1, 1, 1, 1,

                1, 1, false, 1, 1, false, false, 1, false, 1,
                false, false, false, false, false, false, false, false, false, false,
                false, 1, false, 1, false, false, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, false, false, false,
                false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, false, 1, 1, 1, false, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, false, false, false, false, 1,
                1, 1, 1, 1, 1, 1, 1);
    }

    public void DoSimSymbolic(//Symbolic input of Infusion_Manager_Outputs
                              int Commanded_Flow_Rate,
                              int Current_System_Mode,
                              boolean New_Infusion,
                              int Log_Message_ID_1,
                              int Actual_Infusion_Duration,


                              //Symbolic input of Top_Level_Mode_Outputs
                              boolean System_On,
                              boolean Request_Confirm_Stop,
                              int Log_Message_ID_2,


                              //Symbolic input of System_Monitor_Output
                              boolean System_Monitor_Failed,

                              //Symbolic input of System_Monitor_Output
                              int Log,
                              boolean Logging_Failed,

                              //Symbolic input of Operator_Commands
                              boolean System_Start,
                              boolean System_Stop,
                              boolean Infusion_Initiate,
                              boolean Infusion_Inhibit,
                              boolean Infusion_Cancel,
                              boolean Data_Config,
                              boolean Next,
                              boolean Back,
                              boolean Cancel,
                              boolean Keyboard,
                              int Disable_Audio,
                              boolean Notification_Cancel,
                              int Configuration_Type,
                              boolean Confirm_Stop,

                              //Symbolic input of Drug_Database_Inputs
                              boolean Known_Prescription,
                              int Drug_Name1,
                              int Drug_Concentration_High,
                              int Drug_Concentration_Low,
                              int VTBI_High,
                              int VTBI_Low,
                              int Interval_Patient_Bolus,
                              int Number_Max_Patient_Bolus,
                              int Flow_Rate_KVO1,
                              int Flow_Rate_High,
                              int Flow_Rate_Low,

                              //Symbolic input of Device_Sensor_Inputs
                              int Flow_Rate,
                              boolean Flow_Rate_Not_Stable,
                              boolean Air_In_Line,
                              boolean Occlusion,
                              boolean Door_Open,
                              boolean Temp,
                              boolean Air_Pressure,
                              boolean Humidity,
                              boolean Battery_Depleted,
                              boolean Battery_Low,
                              boolean Battery_Unable_To_Charge,
                              boolean Supply_Voltage,
                              boolean CPU_In_Error,
                              boolean RTC_In_Error,
                              boolean Watchdog_Interrupted,
                              boolean Memory_Corrupted,
                              boolean Pump_Too_Hot,
                              boolean Pump_Overheated,
                              boolean Pump_Primed,
                              boolean Post_Successful,

                              //Symbolic input of Device_Configuration_Inputs
                              int Audio_Enable_Duration,
                              int Audio_Level,
                              int Config_Warning_Duration,
                              int Empty_Reservoir,
                              int Low_Reservoir,
                              int Max_Config_Duration,
                              int Max_Duration_Over_Infusion,
                              int Max_Duration_Under_Infusion,
                              int Max_Paused_Duration,
                              int Max_Idle_Duration,
                              int Tolerance_Max,
                              int Tolerance_Min,
                              int Log_Interval,
                              int System_Test_Interval,
                              int Max_Display_Duration,
                              int Max_Confirm_Stop_Duration,

                              //Symbolic input of System_Status_Outputs
                              boolean Reservoir_Empty,
                              int Reservoir_Volume1,
                              int Volume_Infused,
                              int Log_Message_ID3,
                              boolean In_Therapy,

                              //Symbolic input of Config_Outputs
                              int Patient_ID,
                              int Drug_Name2,
                              int Drug_Concentration,
                              int Infusion_Total_Duration,
                              int VTBI_Total,
                              int Flow_Rate_Basal,
                              int Flow_Rate_Intermittent_Bolus,
                              int Duration_Intermittent_Bolus,
                              int Interval_Intermittent_Bolus,
                              int Flow_Rate_Patient_Bolus,
                              int Duration_Patient_Bolus,
                              int Lockout_Period_Patient_Bolus,
                              int Max_Number_of_Patient_Bolus,
                              int Flow_Rate_KVO2,
                              int Entered_Reservoir_Volume,
                              int Reservoir_Volume2,
                              int Configured,
                              int Error_Message_ID,
                              boolean Request_Config_Type,
                              boolean Request_Confirm_Infusion_Initiate,
                              boolean Request_Patient_Drug_Info,
                              boolean Request_Infusion_Info,
                              int Log_Message_ID4,
                              int Config_Timer,
                              int Config_Mode,

                              //Symbolic input of Alarm_Outputs
                              int Is_Audio_Disabled,
                              int Notification_Message,
                              int Audio_Notification_Command,
                              int Highest_Level_Alarm,
                              int Log_Message_ID5,


                              //Symbolic input of Infusion_Manager_Outputs
                              int Commanded_Flow_Rate_2,
                              int Current_System_Mode_2, boolean New_Infusion_2,
                              int Log_Message_ID_1_2,
                              int Actual_Infusion_Duration_2,


                              //Symbolic input of Top_Level_Mode_Outputs
                              boolean System_On_2,
                              boolean Request_Confirm_Stop_2,
                              int Log_Message_ID_2_2,


                              //Symbolic input of System_Monitor_Output
                              boolean System_Monitor_Failed_2,

                              //Symbolic input of System_Monitor_Output
                              int Log_2,
                              boolean Logging_Failed_2,

                              //Symbolic input of Operator_Commands
                              boolean System_Start_2,
                              boolean System_Stop_2,
                              boolean Infusion_Initiate_2,
                              boolean Infusion_Inhibit_2,
                              boolean Infusion_Cancel_2,
                              boolean Data_Config_2,
                              boolean Next_2,
                              boolean Back_2,
                              boolean Cancel_2,
                              boolean Keyboard_2,
                              int Disable_Audio_2,
                              boolean Notification_Cancel_2,
                              int Configuration_Type_2,
                              boolean Confirm_Stop_2,

                              //Symbolic input of Drug_Database_Inputs
                              boolean Known_Prescription_2,
                              int Drug_Name1_2,
                              int Drug_Concentration_High_2,
                              int Drug_Concentration_Low_2,
                              int VTBI_High_2,
                              int VTBI_Low_2,
                              int Interval_Patient_Bolus_2,
                              int Number_Max_Patient_Bolus_2,
                              int Flow_Rate_KVO1_2,
                              int Flow_Rate_High_2,
                              int Flow_Rate_Low_2,

                              //Symbolic input of Device_Sensor_Inputs
                              int Flow_Rate_2,
                              boolean Flow_Rate_Not_Stable_2,
                              boolean Air_In_Line_2,
                              boolean Occlusion_2,
                              boolean Door_Open_2,
                              boolean Temp_2,
                              boolean Air_Pressure_2,
                              boolean Humidity_2,
                              boolean Battery_Depleted_2,
                              boolean Battery_Low_2,
                              boolean Battery_Unable_To_Charge_2,
                              boolean Supply_Voltage_2,
                              boolean CPU_In_Error_2,
                              boolean RTC_In_Error_2,
                              boolean Watchdog_Interrupted_2,
                              boolean Memory_Corrupted_2,
                              boolean Pump_Too_Hot_2,
                              boolean Pump_Overheated_2,
                              boolean Pump_Primed_2,
                              boolean Post_Successful_2,

                              //Symbolic input of Device_Configuration_Inputs
                              int Audio_Enable_Duration_2,
                              int Audio_Level_2,
                              int Config_Warning_Duration_2,
                              int Empty_Reservoir_2,
                              int Low_Reservoir_2,
                              int Max_Config_Duration_2,
                              int Max_Duration_Over_Infusion_2,
                              int Max_Duration_Under_Infusion_2,
                              int Max_Paused_Duration_2,
                              int Max_Idle_Duration_2,
                              int Tolerance_Max_2,
                              int Tolerance_Min_2,
                              int Log_Interval_2,
                              int System_Test_Interval_2,
                              int Max_Display_Duration_2,
                              int Max_Confirm_Stop_Duration_2,

                              //Symbolic input of System_Status_Outputs
                              boolean Reservoir_Empty_2,
                              int Reservoir_Volume1_2,
                              int Volume_Infused_2,
                              int Log_Message_ID3_2,
                              boolean In_Therapy_2,

                              //Symbolic input of Config_Outputs
                              int Patient_ID_2,
                              int Drug_Name2_2,
                              int Drug_Concentration_2,
                              int Infusion_Total_Duration_2,
                              int VTBI_Total_2,
                              int Flow_Rate_Basal_2,
                              int Flow_Rate_Intermittent_Bolus_2,
                              int Duration_Intermittent_Bolus_2,
                              int Interval_Intermittent_Bolus_2,
                              int Flow_Rate_Patient_Bolus_2,
                              int Duration_Patient_Bolus_2,
                              int Lockout_Period_Patient_Bolus_2,
                              int Max_Number_of_Patient_Bolus_2,
                              int Flow_Rate_KVO2_2,
                              int Entered_Reservoir_Volume_2,
                              int Reservoir_Volume2_2,
                              int Configured_2,
                              int Error_Message_ID_2,
                              boolean Request_Config_Type_2,
                              boolean Request_Confirm_Infusion_Initiate_2,
                              boolean Request_Patient_Drug_Info_2,
                              boolean Request_Infusion_Info_2,
                              int Log_Message_ID4_2,
                              int Config_Timer_2,
                              int Config_Mode_2,

                              //Symbolic input of Alarm_Outputs
                              int Is_Audio_Disabled_2,
                              int Notification_Message_2,
                              int Audio_Notification_Command_2,
                              int Highest_Level_Alarm_2,
                              int Log_Message_ID5_2) {
        this.alarm.ALARM_FunctionalSymWrapper(//Symbolic input of Infusion_Manager_Outputs
                Commanded_Flow_Rate,
                Current_System_Mode,  New_Infusion,
                Log_Message_ID_1,
                Actual_Infusion_Duration,


                //Symbolic input of Top_Level_Mode_Outputs
                System_On,
                Request_Confirm_Stop,
                Log_Message_ID_2,


                //Symbolic input of System_Monitor_Output
                System_Monitor_Failed,

                //Symbolic input of System_Monitor_Output
                Log,
                Logging_Failed,

                //Symbolic input of Operator_Commands
                System_Start,
                System_Stop,
                Infusion_Initiate,
                Infusion_Inhibit,
                Infusion_Cancel,
                Data_Config,
                Next,
                Back,
                Cancel,
                Keyboard,
                Disable_Audio,
                Notification_Cancel,
                Configuration_Type,
                Confirm_Stop,

                //Symbolic input of Drug_Database_Inputs
                Known_Prescription,
                Drug_Name1,
                Drug_Concentration_High,
                Drug_Concentration_Low,
                VTBI_High,
                VTBI_Low,
                Interval_Patient_Bolus,
                Number_Max_Patient_Bolus,
                Flow_Rate_KVO1,
                Flow_Rate_High,
                Flow_Rate_Low,

                //Symbolic input of Device_Sensor_Inputs
                Flow_Rate,
                Flow_Rate_Not_Stable,
                Air_In_Line,
                Occlusion,
                Door_Open,
                Temp,
                Air_Pressure,
                Humidity,
                Battery_Depleted,
                Battery_Low,
                Battery_Unable_To_Charge,
                Supply_Voltage,
                CPU_In_Error,
                RTC_In_Error,
                Watchdog_Interrupted,
                Memory_Corrupted,
                Pump_Too_Hot,
                Pump_Overheated,
                Pump_Primed,
                Post_Successful,

                //Symbolic input of Device_Configuration_Inputs
                Audio_Enable_Duration,
                Audio_Level,
                Config_Warning_Duration,
                Empty_Reservoir,
                Low_Reservoir,
                Max_Config_Duration,
                Max_Duration_Over_Infusion,
                Max_Duration_Under_Infusion,
                Max_Paused_Duration,
                Max_Idle_Duration,
                Tolerance_Max,
                Tolerance_Min,
                Log_Interval,
                System_Test_Interval,
                Max_Display_Duration,
                Max_Confirm_Stop_Duration,

                //Symbolic input of System_Status_Outputs
                Reservoir_Empty,
                Reservoir_Volume1,
                Volume_Infused,
                Log_Message_ID3,
                In_Therapy,

                //Symbolic input of Config_Outputs
                Patient_ID,
                Drug_Name2,
                Drug_Concentration,
                Infusion_Total_Duration,
                VTBI_Total,
                Flow_Rate_Basal,
                Flow_Rate_Intermittent_Bolus,
                Duration_Intermittent_Bolus,
                Interval_Intermittent_Bolus,
                Flow_Rate_Patient_Bolus,
                Duration_Patient_Bolus,
                Lockout_Period_Patient_Bolus,
                Max_Number_of_Patient_Bolus,
                Flow_Rate_KVO2,
                Entered_Reservoir_Volume,
                Reservoir_Volume2,
                Configured,
                Error_Message_ID,
                Request_Config_Type,
                Request_Confirm_Infusion_Initiate,
                Request_Patient_Drug_Info,
                Request_Infusion_Info,
                Log_Message_ID4,
                Config_Timer,
                Config_Mode,

                //Symbolic input of Alarm_Outputs
                Is_Audio_Disabled,
                Notification_Message,
                Audio_Notification_Command,
                Highest_Level_Alarm,
                Log_Message_ID5,



                Commanded_Flow_Rate_2,
                Current_System_Mode_2,  New_Infusion_2,
                Log_Message_ID_1_2,
                Actual_Infusion_Duration_2,


                //Symbolic input of Top_Level_Mode_Outputs
                System_On_2,
                Request_Confirm_Stop_2,
                Log_Message_ID_2_2,


                //Symbolic input of System_Monitor_Output
                System_Monitor_Failed_2,

                //Symbolic input of System_Monitor_Output
                Log_2,
                Logging_Failed_2,

                //Symbolic input of Operator_Commands
                System_Start_2,
                System_Stop_2,
                Infusion_Initiate_2,
                Infusion_Inhibit_2,
                Infusion_Cancel_2,
                Data_Config_2,
                Next_2,
                Back_2,
                Cancel_2,
                Keyboard_2,
                Disable_Audio_2,
                Notification_Cancel_2,
                Configuration_Type_2,
                Confirm_Stop_2,

                //Symbolic input of Drug_Database_Inputs
                Known_Prescription_2,
                Drug_Name1_2,
                Drug_Concentration_High_2,
                Drug_Concentration_Low_2,
                VTBI_High_2,
                VTBI_Low_2,
                Interval_Patient_Bolus_2,
                Number_Max_Patient_Bolus_2,
                Flow_Rate_KVO1_2,
                Flow_Rate_High_2,
                Flow_Rate_Low_2,

                //Symbolic input of Device_Sensor_Inputs
                Flow_Rate_2,
                Flow_Rate_Not_Stable_2,
                Air_In_Line_2,
                Occlusion_2,
                Door_Open_2,
                Temp_2,
                Air_Pressure_2,
                Humidity_2,
                Battery_Depleted_2,
                Battery_Low_2,
                Battery_Unable_To_Charge_2,
                Supply_Voltage_2,
                CPU_In_Error_2,
                RTC_In_Error_2,
                Watchdog_Interrupted_2,
                Memory_Corrupted_2,
                Pump_Too_Hot_2,
                Pump_Overheated_2,
                Pump_Primed_2,
                Post_Successful_2,

                //Symbolic input of Device_Configuration_Inputs
                Audio_Enable_Duration_2,
                Audio_Level_2,
                Config_Warning_Duration_2,
                Empty_Reservoir_2,
                Low_Reservoir_2,
                Max_Config_Duration_2,
                Max_Duration_Over_Infusion_2,
                Max_Duration_Under_Infusion_2,
                Max_Paused_Duration_2,
                Max_Idle_Duration_2,
                Tolerance_Max_2,
                Tolerance_Min_2,
                Log_Interval_2,
                System_Test_Interval_2,
                Max_Display_Duration_2,
                Max_Confirm_Stop_Duration_2,

                //Symbolic input of System_Status_Outputs
                Reservoir_Empty_2,
                Reservoir_Volume1_2,
                Volume_Infused_2,
                Log_Message_ID3_2,
                In_Therapy_2,

                //Symbolic input of Config_Outputs
                Patient_ID_2,
                Drug_Name2_2,
                Drug_Concentration_2,
                Infusion_Total_Duration_2,
                VTBI_Total_2,
                Flow_Rate_Basal_2,
                Flow_Rate_Intermittent_Bolus_2,
                Duration_Intermittent_Bolus_2,
                Interval_Intermittent_Bolus_2,
                Flow_Rate_Patient_Bolus_2,
                Duration_Patient_Bolus_2,
                Lockout_Period_Patient_Bolus_2,
                Max_Number_of_Patient_Bolus_2,
                Flow_Rate_KVO2_2,
                Entered_Reservoir_Volume_2,
                Reservoir_Volume2_2,
                Configured_2,
                Error_Message_ID_2,
                Request_Config_Type_2,
                Request_Confirm_Infusion_Initiate_2,
                Request_Patient_Drug_Info_2,
                Request_Infusion_Info_2,
                Log_Message_ID4_2,
                Config_Timer_2,
                Config_Mode_2,

                //Symbolic input of Alarm_Outputs
                Is_Audio_Disabled_2,
                Notification_Message_2,
                Audio_Notification_Command_2,
                Highest_Level_Alarm_2,
                Log_Message_ID5_2);
    }

    public static void main(String[] args) {
        ALARM_FunctionalMain alarmMain;
        if (args.length < 2) { // Run symbolically if no args
            alarmMain = new ALARM_FunctionalMain();
            alarmMain.DoSimSymbolic();
        }
        // else {
        // rjcmain = new RJCMain(args[0], args[1]);
        // rjcmain.DoSim();
        // }
        else {
            alarmMain = new ALARM_FunctionalMain();
            alarmMain.DoSimSymbolic(//Symbolic input of Infusion_Manager_Outputs
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Boolean.parseBoolean(args[2]),
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4]),


                    //Symbolic input of Top_Level_Mode_Outputs
                    Boolean.parseBoolean(args[5]),
                    Boolean.parseBoolean(args[6]),
                    Integer.parseInt(args[7]),


                    //Symbolic input of System_Monitor_Output
                    Boolean.parseBoolean(args[8]),

                    //Symbolic input of System_Monitor_Output
                    Integer.parseInt(args[9]),
                    Boolean.parseBoolean(args[10]),

                    //Symbolic input of Operator_Commands
                    Boolean.parseBoolean(args[11]),
                    Boolean.parseBoolean(args[12]),
                    Boolean.parseBoolean(args[13]),
                    Boolean.parseBoolean(args[14]),
                    Boolean.parseBoolean(args[15]),
                    Boolean.parseBoolean(args[16]),
                    Boolean.parseBoolean(args[17]),
                    Boolean.parseBoolean(args[18]),
                    Boolean.parseBoolean(args[19]),
                    Boolean.parseBoolean(args[20]),
                    Integer.parseInt(args[21]),
                    Boolean.parseBoolean(args[22]),
                    Integer.parseInt(args[23]),
                    Boolean.parseBoolean(args[24]),

                    //Symbolic input of Drug_Database_Inputs
                    Boolean.parseBoolean(args[25]),
                    Integer.parseInt(args[26]),
                    Integer.parseInt(args[27]),
                    Integer.parseInt(args[28]),
                    Integer.parseInt(args[29]),
                    Integer.parseInt(args[30]),
                    Integer.parseInt(args[31]),
                    Integer.parseInt(args[32]),
                    Integer.parseInt(args[33]),
                    Integer.parseInt(args[34]),
                    Integer.parseInt(args[35]),

                    //Symbolic input of Device_Sensor_Inputs
                    Integer.parseInt(args[36]),
                    Boolean.parseBoolean(args[37]),
                    Boolean.parseBoolean(args[38]),
                    Boolean.parseBoolean(args[39]),
                    Boolean.parseBoolean(args[40]),
                    Boolean.parseBoolean(args[41]),
                    Boolean.parseBoolean(args[42]),
                    Boolean.parseBoolean(args[43]),
                    Boolean.parseBoolean(args[44]),
                    Boolean.parseBoolean(args[45]),
                    Boolean.parseBoolean(args[46]),
                    Boolean.parseBoolean(args[47]),
                    Boolean.parseBoolean(args[48]),
                    Boolean.parseBoolean(args[49]),
                    Boolean.parseBoolean(args[50]),
                    Boolean.parseBoolean(args[51]),
                    Boolean.parseBoolean(args[52]),
                    Boolean.parseBoolean(args[53]),
                    Boolean.parseBoolean(args[54]),
                    Boolean.parseBoolean(args[55]),

                    //Symbolic input of Device_Configuration_Inputs
                    Integer.parseInt(args[56]),
                    Integer.parseInt(args[57]),
                    Integer.parseInt(args[58]),
                    Integer.parseInt(args[59]),
                    Integer.parseInt(args[60]),
                    Integer.parseInt(args[61]),
                    Integer.parseInt(args[62]),
                    Integer.parseInt(args[63]),
                    Integer.parseInt(args[64]),
                    Integer.parseInt(args[65]),
                    Integer.parseInt(args[66]),
                    Integer.parseInt(args[67]),
                    Integer.parseInt(args[68]),
                    Integer.parseInt(args[69]),
                    Integer.parseInt(args[70]),
                    Integer.parseInt(args[71]),

                    //Symbolic input of System_Status_Outputs
                    Boolean.parseBoolean(args[72]),
                    Integer.parseInt(args[73]),
                    Integer.parseInt(args[74]),
                    Integer.parseInt(args[75]),
                    Boolean.parseBoolean(args[76]),

                    //Symbolic input of Config_Outputs
                    Integer.parseInt(args[77]),
                    Integer.parseInt(args[78]),
                    Integer.parseInt(args[79]),
                    Integer.parseInt(args[80]),
                    Integer.parseInt(args[81]),
                    Integer.parseInt(args[82]),
                    Integer.parseInt(args[83]),
                    Integer.parseInt(args[84]),
                    Integer.parseInt(args[85]),
                    Integer.parseInt(args[86]),
                    Integer.parseInt(args[87]),
                    Integer.parseInt(args[88]),
                    Integer.parseInt(args[89]),
                    Integer.parseInt(args[90]),
                    Integer.parseInt(args[91]),
                    Integer.parseInt(args[92]),
                    Integer.parseInt(args[93]),
                    Integer.parseInt(args[94]),
                    Boolean.parseBoolean(args[95]),
                    Boolean.parseBoolean(args[96]),
                    Boolean.parseBoolean(args[97]),
                    Boolean.parseBoolean(args[98]),
                    Integer.parseInt(args[99]),
                    Integer.parseInt(args[100]),
                    Integer.parseInt(args[101]),

                    //Symbolic input of Alarm_Outputs
                    Integer.parseInt(args[102]),
                    Integer.parseInt(args[103]),
                    Integer.parseInt(args[104]),
                    Integer.parseInt(args[105]),
                    Integer.parseInt(args[106]),



            //second step param

                    Integer.parseInt(args[107]),
                    Integer.parseInt(args[108]),
                    Boolean.parseBoolean(args[109]),
                    Integer.parseInt(args[110]),
                    Integer.parseInt(args[111]),


                    //Symbolic input of Top_Level_Mode_Outputs
                    Boolean.parseBoolean(args[112]),
                    Boolean.parseBoolean(args[113]),
                    Integer.parseInt(args[114]),


                    //Symbolic input of System_Monitor_Output
                    Boolean.parseBoolean(args[115]),

                    //Symbolic input of System_Monitor_Output
                    Integer.parseInt(args[116]),
                    Boolean.parseBoolean(args[117]),

                    //Symbolic input of Operator_Commands
                    Boolean.parseBoolean(args[118]),
                    Boolean.parseBoolean(args[119]),
                    Boolean.parseBoolean(args[120]),
                    Boolean.parseBoolean(args[121]),
                    Boolean.parseBoolean(args[122]),
                    Boolean.parseBoolean(args[123]),
                    Boolean.parseBoolean(args[124]),
                    Boolean.parseBoolean(args[125]),
                    Boolean.parseBoolean(args[126]),
                    Boolean.parseBoolean(args[127]),
                    Integer.parseInt(args[128]),
                    Boolean.parseBoolean(args[129]),
                    Integer.parseInt(args[130]),
                    Boolean.parseBoolean(args[131]),

                    //Symbolic input of Drug_Database_Inputs
                    Boolean.parseBoolean(args[132]),
                    Integer.parseInt(args[133]),
                    Integer.parseInt(args[134]),
                    Integer.parseInt(args[135]),
                    Integer.parseInt(args[136]),
                    Integer.parseInt(args[137]),
                    Integer.parseInt(args[137]),
                    Integer.parseInt(args[138]),
                    Integer.parseInt(args[139]),
                    Integer.parseInt(args[140]),
                    Integer.parseInt(args[141]),

                    //Symbolic input of Device_Sensor_Inputs
                    Integer.parseInt(args[142]),
                    Boolean.parseBoolean(args[143]),
                    Boolean.parseBoolean(args[144]),
                    Boolean.parseBoolean(args[145]),
                    Boolean.parseBoolean(args[146]),
                    Boolean.parseBoolean(args[147]),
                    Boolean.parseBoolean(args[148]),
                    Boolean.parseBoolean(args[149]),
                    Boolean.parseBoolean(args[150]),
                    Boolean.parseBoolean(args[151]),
                    Boolean.parseBoolean(args[152]),
                    Boolean.parseBoolean(args[153]),
                    Boolean.parseBoolean(args[154]),
                    Boolean.parseBoolean(args[155]),
                    Boolean.parseBoolean(args[156]),
                    Boolean.parseBoolean(args[157]),
                    Boolean.parseBoolean(args[158]),
                    Boolean.parseBoolean(args[159]),
                    Boolean.parseBoolean(args[160]),
                    Boolean.parseBoolean(args[161]),

                    //Symbolic input of Device_Configuration_Inputs
                    Integer.parseInt(args[162]),
                    Integer.parseInt(args[163]),
                    Integer.parseInt(args[164]),
                    Integer.parseInt(args[165]),
                    Integer.parseInt(args[166]),
                    Integer.parseInt(args[167]),
                    Integer.parseInt(args[168]),
                    Integer.parseInt(args[169]),
                    Integer.parseInt(args[170]),
                    Integer.parseInt(args[171]),
                    Integer.parseInt(args[172]),
                    Integer.parseInt(args[173]),
                    Integer.parseInt(args[174]),
                    Integer.parseInt(args[175]),
                    Integer.parseInt(args[176]),
                    Integer.parseInt(args[177]),

                    //Symbolic input of System_Status_Outputs
                    Boolean.parseBoolean(args[178]),
                    Integer.parseInt(args[179]),
                    Integer.parseInt(args[180]),
                    Integer.parseInt(args[181]),
                    Boolean.parseBoolean(args[182]),

                    //Symbolic input of Config_Outputs
                    Integer.parseInt(args[183]),
                    Integer.parseInt(args[184]),
                    Integer.parseInt(args[185]),
                    Integer.parseInt(args[186]),
                    Integer.parseInt(args[187]),
                    Integer.parseInt(args[189]),
                    Integer.parseInt(args[190]),
                    Integer.parseInt(args[191]),
                    Integer.parseInt(args[192]),
                    Integer.parseInt(args[193]),
                    Integer.parseInt(args[194]),
                    Integer.parseInt(args[195]),
                    Integer.parseInt(args[196]),
                    Integer.parseInt(args[197]),
                    Integer.parseInt(args[198]),
                    Integer.parseInt(args[199]),
                    Integer.parseInt(args[200]),
                    Integer.parseInt(args[201]),
                    Boolean.parseBoolean(args[202]),
                    Boolean.parseBoolean(args[203]),
                    Boolean.parseBoolean(args[204]),
                    Boolean.parseBoolean(args[205]),
                    Integer.parseInt(args[206]),
                    Integer.parseInt(args[207]),
                    Integer.parseInt(args[208]),

                    //Symbolic input of Alarm_Outputs
                    Integer.parseInt(args[209]),
                    Integer.parseInt(args[210]),
                    Integer.parseInt(args[211]),
                    Integer.parseInt(args[212]),
                    Integer.parseInt(args[213]));
        }
    }
}
