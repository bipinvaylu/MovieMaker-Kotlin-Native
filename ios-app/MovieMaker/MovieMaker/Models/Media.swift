//
//  Media.swift
//  VideoPicker
//
//  Created by AJit on 7/19/18.
//  Copyright © 2018 AJit. All rights reserved.
//

import Foundation

public class Media{
    
    var url: String
    var createdDate: Double
    
    init(url: String, createdDate: Double) {
        self.url = url
        self.createdDate = createdDate
    }
    
    func getUrl() -> URL{
        return URL(string: url)!
    }
}
