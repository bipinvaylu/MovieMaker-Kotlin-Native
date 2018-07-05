//
//  ViewController.swift
//  MovieMaker
//
//  Created by Bipin Vaylu on 05/07/18.
//  Copyright © 2018 Bipin Vaylu. All rights reserved.
//

import UIKit
import KotlinMovieMaker

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        print("\(KMMHello().get())")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

