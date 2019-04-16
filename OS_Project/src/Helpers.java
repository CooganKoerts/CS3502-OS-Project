public class Helpers {
    public static String hex_to_binary(String s)
    {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }

        String val2 = Integer.toBinaryString(val);
        return val2;
    }

    public static int hex_to_decimal(String s)
    {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }

    public static int binary_to_Integer(String binary) {
        char[] numbers = binary.toCharArray();
        int result = 0;
        for(int i=numbers.length - 1; i>=0; i--)
            if(numbers[i]=='1')
                result += Math.pow(2, (numbers.length-i - 1));
        return result;
    }


    public static boolean isBinary(String num) {
        boolean isBinary = false;
        if (num != null && !num.isEmpty())
        {
            long number = (Long) Long.parseLong(num);
            while (number > 0) {
                if (number % 10 <= 1) {
                    isBinary = true;
                }
                else {
                    isBinary = false;
                    break;
                }
                number /= 10;
            }
        }
        return isBinary;
    }

    public static int binary_to_decimal(String binary) {
        int length = binary.length() - 1;
        int decimal = 0;
        if (isBinary(binary)) {
            char[] digits = binary.toCharArray();
            for (char digit : digits) {
                if (String.valueOf(digit).equals("1")) {
                    decimal += Math.pow(2, length);
                }
                length--;
            }
        }
        return decimal;
    }

    public static String binary_to_hexadecimal(String binary) {
        String hexa = "";
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        if (binary != null && !binary.isEmpty()) {
            int decimal = binary_to_decimal(binary);
            while (decimal > 0) {
                hexa = hex[decimal % 16] + hexa;
                decimal /= 16;
            }
        }
        return hexa;
    }

    public static String decimal_to_hex(int decimal){
        int sizeOfIntInHalfBytes = 8;
        int numberOfBitsInAHalfByte = 4;
        int halfByte = 0x0F;
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
        {
            int j = decimal & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            decimal >>= numberOfBitsInAHalfByte;
        }
        return hexBuilder.toString();
    }
}
