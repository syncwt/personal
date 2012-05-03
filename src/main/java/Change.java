import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


public class Change {

  static class OneChange {
    int denomination;
    int count;

    public OneChange(Integer denomination, int count) {
      this.denomination = denomination;
      this.count = count;
    }

    public void addCoin(int n) {
      count += n;
    }

    @Override
    public String toString() {
      return count + "x" + denomination;
    }

  }

  static class Solution {
    private Map<Integer, OneChange> denominations = Maps.newHashMap();

    public void addCoin(Integer denomination) {
      OneChange c = denominations.get(denomination);
      if (c == null) {
        c = new OneChange(denomination, 1);
        denominations.put(denomination, c);
      } else {
        c.addCoin(c.count);
      }
    }

    public void addSolution(Solution sol) {
      for (OneChange oc : sol.getCoins()) {
        addCoin(oc.denomination);
      }
    }

    private Collection<OneChange> getCoins() {
      return denominations.values();
    }

    @Override
    public String toString() {
      StringBuilder result = new StringBuilder();
      result.append("[");
      for (OneChange oc : denominations.values()) {
        result.append(oc + " ");
      }
      result.append("]");
      return result.toString();
    }
  }

  static void f2(List<Integer> denominations, int value, List<Solution> solutions) {
    for (Integer d : denominations) {
      List<Solution> previous = Lists.newArrayList();
      if (value > 0 && d <= value) {
        f2(denominations, value - d.intValue(), previous);
        if (previous.isEmpty()) {
          Solution singleSolution = new Solution();
          singleSolution.addCoin(d);
          solutions.add(singleSolution);
        } else {
          for (Solution sol : previous) {
            Solution singleSolution = new Solution();
            singleSolution.addCoin(d);
            singleSolution.addSolution(sol);
            solutions.add(singleSolution);
          }
        }
      }
    }
  }

  static void f(List<Integer> denominations, int value, List<List<Integer>> result) {
    System.out.println("f(" + denominations + ", " + value + ")");
    for (Integer d : denominations) {
      List<Integer> cl = Lists.newArrayList(denominations);
      if (value > 0 && d <= value) {
//        cl.remove(d);
        List<List<Integer>> previous = Lists.newArrayList();
        f(cl, value - d.intValue(), previous);
        if (previous.isEmpty()) {
          result.add(Lists.newArrayList(d));
        } else {
          for (List<Integer> sol : previous) {
            List<Integer> newSolution = Lists.newArrayList(d);
//            System.out.println("Adding to singlelist " + d + " the list:" + sol);
            newSolution.addAll(sol);
            result.add(newSolution);
          }
        }
      }
    }
//    System.out.println("Temp result for(" + denominations + ", " + value + "): " + result);
  }

  public static void main(String[] args) {
//    List<List<Integer>> result = Lists.newArrayList();
//    f(ImmutableList.of(25, 10, 5, 1), 22, result);
    List<Solution> result2 = Lists.newArrayList();
    f2(ImmutableList.of(1), 5, result2);
    System.out.println(Sets.newHashSet(result2));
  }
}
