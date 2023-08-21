mod array_my {
    extern crate ndarray;

    use ndarray::Array;

    fn test_array() {
        let aa = 32 as i64;
        let a = Array::from_vec(vec![1., 2., 3., 4., 5.]);
        let b = Array::from_vec(vec![5., 4., 3., 2., 1.]);
        let mut c = Array::from_vec(vec![1., 2., 3., 4., 5.]);
        let mut d = Array::from_vec(vec![5., 4., 3., 2., 1.]);

        let z = a + b;
        let w = &c + &d;

        let epsilon = 1e-8;
        for elem in z.iter() {
            let diff: f32 = *elem - 6.;
            assert!(diff.abs() < epsilon);
        }

        println!("c = {}", c);
        c[0] = 10.;
        d[1] = 10.;

        for elem in w.iter() {
            let diff: f32 = *elem - 6.;
            assert!(diff.abs() < epsilon);
        }
    }

    #[test]
    fn test_ams() {
        test_array();
        // This binding lives in the main function
        let long_lived_binding = 1;
        // This is a block, and has a smaller scope than the main function
        {
            // This binding only exists in this block
            let short_lived_binding = 2;
            println!("inner short: {}", short_lived_binding);
            // This binding *shadows* the outer one
            let long_lived_binding = 5_f32;
            println!("inner long: {}", long_lived_binding);
        }
        // End of the block
        // Error! `short_lived_binding` doesn't exist in this scope
        //        println!("outer short: {}", short_lived_binding);
        // FIXME ^ Comment out this line
        println!("outer long: {}", long_lived_binding); //等于几？
                                                        // This binding also *shadows* the previous binding
        let long_lived_binding = 'a';
        println!("outer long: {}", long_lived_binding);
    }
}
