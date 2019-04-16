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

        // Left pad with 0s
        String zeros = "";
        if (val2.length() < 32)
        {
            int zerosToAdd = 32 - val2.length();
            for (int i = 0; i < zerosToAdd; i++)
            {
                zeros += "0";
            }
        }
        val2 = zeros + val2;
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


    public static int binary_to_decimal(String binary)
    {
        int decimal = 0;
        for (int pow = (binary.length()-1); pow > -1; pow--) {
            if (binary.charAt(pow)=='1'){
                decimal += (Math.pow(2, (binary.length() - pow - 1)));
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
