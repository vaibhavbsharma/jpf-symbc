public class IssueTest {

  public static void main(String[] args) {
    hello("Goodbye, World!");
  }

  public static int hello(String var_1) {
    int i = 0;
    if (var_1.contains("i")) {
      return 1;
    }
    if (var_1.length() > 20) {
      return 5;
    }
    if (var_1.endsWith("a")) {
      return 6;
    }
    if (var_1.equals("Goodbye, World!")) {
      return 3;
    }
    return 4;
  }
}

