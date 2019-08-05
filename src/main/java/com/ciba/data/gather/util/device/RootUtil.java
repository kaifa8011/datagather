package com.ciba.data.gather.util.device;

/**
 * @author : ciba
 * @date : 2018/7/23
 * @description : replace your description
 */

import com.ciba.data.gather.util.DataGatherLog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * RootUtil
 */
public class RootUtil {
    private static final String COMMAND_SU = "su";
    private static final String COMMAND_SH = "sh";
    private static final String COMMAND_EXIT = "exit\n";
    private static final String COMMAND_LINE_END = "\n";

    /**
     * 检查是否有Root
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isRoot, isNeedResultMsg);
    }

    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                    os = null;
                }
                if (successResult != null) {
                    successResult.close();
                    successResult = null;
                }
                if (errorResult != null) {
                    errorResult.close();
                    errorResult = null;
                }
            } catch (Exception e) {
                DataGatherLog.innerI(e.getMessage());
            }


            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    public static class CommandResult {
        public int result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}
