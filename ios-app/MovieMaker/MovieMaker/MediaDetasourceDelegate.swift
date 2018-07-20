//
//  MediaDetasourceDelegate.swift
//  VideoPicker
//
//  Created by AJit on 7/19/18.
//  Copyright © 2018 AJit. All rights reserved.
//

import Foundation
import UIKit
import AVFoundation
import KotlinMovieMaker

class MediaDatasourceDelegate: NSObject, UICollectionViewDelegate, UICollectionViewDataSource {
    
    
    var items = [KMMBasicMedia]()
    
    public func setItems(items: [KMMBasicMedia]) {
        self.items = items
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "id_video_cell", for: indexPath) as! VideoCell
        let url = URL(string: items[indexPath.item].path)!
        let asset = AVURLAsset(url: url)
        
        let imageGenerator = AVAssetImageGenerator(asset: asset)
        let time = CMTime(seconds: 1, preferredTimescale: 1)
        
        do {
            let imageRef = try imageGenerator.copyCGImage(at: time, actualTime: nil)
            cell.thumbnail.image = UIImage(cgImage: imageRef)
        } catch {
            print(error)
            // return UIImage(named: "some generic thumbnail")!
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return items.count
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
    }
    
}

class VideoCell: UICollectionViewCell{
    
    @IBOutlet var thumbnail: UIImageView!
    
    
    
}

