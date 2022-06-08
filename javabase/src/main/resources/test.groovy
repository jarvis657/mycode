/**
 * @Author:lmq
 * @Date: 2022/4/21
 * @Desc:
 * */
class test {
    test() {
        String.metaClass.executeCmd = { silent ->
            //make sure that all paramters are interpreted through the cmd-shell
            //TODO: make this also work with *nix
            def p = "cmd /c ${delegate.value}".execute()
            def result = [std: '', err: '']
            def ready = false
            Thread.start {
                def reader = new BufferedReader(new InputStreamReader(p.in))
                def line = ""
                while ((line = reader.readLine()) != null) {
                    if (silent != false) {
                        println "" + line
                    }
                    result.std += line + "\n"
                }
                ready = true
                reader.close()
            }
            p.waitForOrKill(30000)
            def error = p.err.text
            if (error.isEmpty()) {
                return result
            } else {
                throw new RuntimeException("\n" + error)
            }
        }
    }
}
