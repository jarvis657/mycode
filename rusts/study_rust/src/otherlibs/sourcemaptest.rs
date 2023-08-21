use sourcemap::SourceMap;
pub fn test_sourcemap() {
    let input: &[_] = b"{
    \"version\":3,
    \"sources\":[\"coolstuff.js\"],
    \"names\":[\"x\",\"alert\"],
    \"mappings\":\"AAAA,GAAIA,GAAI,EACR,IAAIA,GAAK,EAAG,CACVC,MAAM\"
}";
    let sm = SourceMap::from_reader(input).unwrap();
    let token = sm.lookup_token(1, 1).unwrap(); // line-number and column
    println!("token: {}", token);
}
