//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package jp.co.omron.healthcare.oc.device.ohq;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OHQWlCommand implements Cloneable {
    public static final char ACCESS_END_COMMAND_CODE = '\u000f';
    public static final char ACCESS_END_RES_COMMAND_CODE = '\u008f';
    public static final char ACCESS_START_COMMAND_CODE = '\u0000';
    public static final char ACCESS_START_RES_COMMAND_CODE = '\u0080';
    public static final char CHKSUM_SBCC_SIZE = '\u0002';
    public static final char IDX_COMMAND_CODE = '\u0001';
    public static final char SINGLE_ACCESS_COMMAND_CODE = '\u0001';
    public static final char SINGLE_ACCESS_RES_COMMAND_CODE = '\u0081';
    public static final int WL_BLOCK_COM_WRITE_TIME_INTERVAL_DEFAULT = 150;
    public static final int WL_COMMAND_RETRY_NUM_DEFAULT = 3;
    public static final int WL_COMMAND_SEND_TIMEOUT_DEFAULT = 10;
    private static final String a = OHQWlCommand.class.getSimpleName();
    private int b = 10;
    private int c = 3;
    private byte[] d = null;
    private Map e = new HashMap();
    private byte[] f = null;
    private boolean g = false;
    private boolean h = false;

    public OHQWlCommand(int var1, int var2) {
        this.b = var1;
        this.c = var2;
    }

    private byte a(boolean var1) {
        if(var1) {
            return (byte)16;
        } else {
            return (byte)17;
        }
    }

    private byte a(byte[] var1, int var2) {
        int var4 = 0;

        byte var3;
        for(var3 = 0; var4 < var2; ++var4) {
            var3 ^= var1[var4];
        }

        return var3;
    }

    private void a(int var1, int var2, byte[] var3) {
        this.b = var1;
        this.c = var2;
        System.arraycopy(var3, 0, this.d, 0, var3.length);
    }

    private byte[] a(byte[] var1, byte[] var2) {
        byte var3 = (byte)(var1[0] | var2[0]);
        byte var4 = (byte)(var1[1] | var2[1]);
        byte var5 = (byte)(var1[2] | var2[2]);
        return new byte[]{var3, var4, var5};
    }

    private byte b(boolean var1) {
        if(var1) {
            return (byte)0;
        } else {
            return (byte)1;
        }
    }

    private byte[] c(boolean var1) {
        byte[] var2 = new byte[3];
        if(var1) {
            var2[0] = 0;
        } else {
            var2[0] = -64;
        }

        return var2;
    }

    public int byte2Int(byte var1) {
        int var2 = var1;
        if(var1 < 0) {
            var2 = var1 + 256;
        }

        return var2;
    }

    public OHQWlCommand clone() {

        OHQWlCommand var1;
        try {
            var1 = (OHQWlCommand)super.clone();
        } catch (CloneNotSupportedException var2) {
            return null;
        }

        var1.a(this.b, this.c, this.d);
        return var1;
    }

    public byte[] createAccessEnd(boolean var1) {
        //确认  080F000000000007
        this.h = var1;
        byte[] var2 = new byte[8];
        var2[0] = 8;
        var2[1] = 15;
        var2[5] = this.b(this.h);
        var2[6] = 0;
        var2[7] = this.a(var2, 7);
        return var2;
    }

    public byte[] createAccessEndError(boolean var1) {
        this.h = var1;
        byte[] var2 = new byte[]{(byte)8, (byte)15, (byte)-1, (byte)-1, (byte)-1, this.b(this.h), (byte)0, (byte)0};
        var2[7] = this.a(var2, 7);
        return var2;
    }

    public byte[] createAccessStart(boolean var1) {
        //启动2 ? 0800000000100018
        this.g = var1;
        byte[] var2 = new byte[8];
        var2[0] = 8;
        var2[1] = 0;
        var2[5] = this.a(this.g);
        var2[6] = 0;
        var2[7] = this.a(var2, 7);
        return var2;
    }

    public byte[] createSingleAccess(byte[] var1, byte[] var2, byte var3, boolean var4) {
        //查询码  08010004500E0053
//         I/tool_xu_write8: printBYTE: 00 04 96
//        I/tool_xu_byte: printByte: e
//        I/tool_xu_boolean: printBoolean: true
//        I/tool_xu_write5: printBYTE: 08 01 00 04 96 0E 00 95
        if(var1 != null && var1.length == 3) {
            if((var1[0] & 240) != 0) {
                return null;
            } else {
                if(!var4) {
                    if(var2 == null) {
                        return null;
                    }

                    if(var2.length != var3) {
                        return null;
                    }
                }

                int var6;
                if(var4) {
                    var6 = 8;
                } else {
                    var6 = var3 + 8;
                }

                byte[] var9 = new byte[var6];
                var9[0] = (byte)var6;
                var9[1] = 1;
                var1 = this.a(this.c(var4), var1);
                var9[2] = var1[0];
                var9[3] = var1[1];
                var9[4] = var1[2];
                var9[5] = var3;
                int var5;
                int var8;
                if(!var4) {
                    var5 = 6;
                    int var7 = 0;

                    while(true) {
                        var8 = var5;
                        if(var7 >= var2.length) {
                            break;
                        }

                        var9[var5] = var2[var7];
                        ++var7;
                        ++var5;
                    }
                } else {
                    var8 = 6;
                }

                var5 = var8 + 1;
                var9[var8] = 0;
                var9[var5] = this.a(var9, var6 - 1);
                return var9;
            }
        } else {
            return null;
        }
    }

    public byte[] getAddressFromCommand(byte[] var1) {
        int var5 = var1.length;
        int var6 = this.byte2Int(var1[1]);
        if(var6 != 1 && var6 != 129) {
            return null;
        } else if(var1[0] == var5 && var5 >= 5) {
            byte var2 = (byte)(var1[2] & 15);
            byte var3 = var1[3];
            byte var4 = var1[4];
            return new byte[]{var2, var3, var4};
        } else {
            return null;
        }
    }

    public int getCommandCode() {
        int var1 = this.byte2Int(this.d[1]);
        return var1;
    }

    public byte[] getDataFromResCommandPacketData() {
        Object var4 = null;
        byte[] var3 = (byte[])var4;
        if(this.f != null) {
            int var1 = this.byte2Int(this.f[1]);
            int var2 = this.byte2Int(this.f[0]);
            if(128 == var1) {
                var1 = var2 - 8;
                var3 = new byte[var1];
                System.arraycopy(this.f, 6, var3, 0, var1);
            } else if(129 == var1) {
                var1 = var2 - 8;
                var3 = new byte[var1];
                System.arraycopy(this.f, 6, var3, 0, var1);
            } else {
                var3 = (byte[])var4;
            }
        }

        return var3;
    }

    public int getDataSizeFromCommand(byte[] var1) {
        int var2 = var1.length;
        int var3 = this.byte2Int(var1[1]);
        if(var3 != 1 && var3 != 129) {
            return 0;
        } else if(var1[0] == var2 && var2 >= 6) {
            byte var4 = var1[5];
            return var4;
        } else {
            return 0;
        }
    }

    public byte[] getReqCommandPacketData() {
        return this.d;
    }

    public byte[] getResCommandPacketData() {
        return this.f;
    }

    public byte getResultCodeFromResCommandPacketData() {
        byte var1 = -1;
        if(this.f != null) {
            var1 = this.f[this.f.length - 2];
        }

        return var1;
    }

    public int getWlCommandRetryNum() {
        return this.c;
    }

    public int getWlCommandSendingTimeout() {
        return this.b;
    }

    public void setReceiveData(UUID var1, byte[] var2) {
        this.e.put(var1.toString(), var2);
    }

    public void setReqCommandPacketData(byte[] var1) {
        this.d = var1;
    }

    public void setResCommandPacketData(byte[] var1) {
        this.f = var1;
    }

    public boolean verifyBcc(byte[] var1) {
        boolean var5 = true;
        if(var1 != null && var1.length >= 2) {
            int var4 = var1.length;
            int var2 = 0;

            byte var3;
            for(var3 = 0; var2 < var4 - 1; ++var2) {
                var3 ^= var1[var2];
            }

            if(var3 != var1[var4 - 1]) {
                return false;
            }
        } else {
            var5 = false;
        }

        return var5;
    }
}
