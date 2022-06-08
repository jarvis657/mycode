package origin.jdk8.lambda;

import graphql.validation.rules.ArgumentsOfCorrectType;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bouncycastle.util.Strings;

/**
 * @Author:lmq
 * @Date: 2022/3/11
 * @Desc:
 **/
public class Completure {

    public static void main(String[] args) {
//        Arrays.asList("1_1", "2_2", "3_3", "4_4","5_5").stream().map(date -> {
////            List<CodeSpecWarningOfUserBo> users = codeSpecWarningService.queryCodeWarningOfUser(
////                    request.convertToOrgQuery());
//            String[] s = Strings.split(date, '_');
//            List<String> users = new ArrayList<>();
//            for (int i = 0; i < Integer.parseInt(s[1]); i++) {
//                users.add(s[0]+":"+i);
//            }
//
//            CodeSpecWarningOfUserBo total = users.stream().reduce(new CodeSpecWarningOfUserBo(), (c, u) -> {
//                c.setCommonNumber(MathUtil.plus(c.getCommonNumber(), u.getCommonNumber()));
//                c.setPromptNumber(MathUtil.plus(c.getPromptNumber(), u.getPromptNumber()));
//                c.setSeriousNumber(MathUtil.plus(c.getSeriousNumber(), u.getSeriousNumber()));
//                c.setTotalNumber(MathUtil.plus(c.getTotalNumber(), u.getTotalNumber()));
//                return c;
//            }, (u1, u2) -> u1);
//            total.setHandler(date);
//            return (Entry<String, CodeSpecWarningOfUserBo>) new SimpleEntry<>(date, total);
//        }).collect(Collectors.toConcurrentMap(Entry::getKey, Entry::getValue))
    }

}
