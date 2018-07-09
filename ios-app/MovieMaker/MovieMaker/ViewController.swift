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
        let timeInMillis: Int64 =
            Int64((NSDate().timeIntervalSince1970 * 1000.0).rounded())
        let media = KMMMediaImage(path: "PATH", thmPath: "THMPath", createdDate: timeInMillis, fileSize: 12345)
        print("\(media)")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

