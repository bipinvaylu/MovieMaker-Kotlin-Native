//
//  ViewController.swift
//  VideoPicker
//
//  Created by AJit on 7/19/18.
//  Copyright © 2018 AJit. All rights reserved.
//

import UIKit
import KotlinMovieMaker

class ViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    var movieCollection = [KMMBasicMedia](){
        didSet{
            exportButton.isEnabled = movieCollection.count >= 2
        }
    }
    
    var mediaDatasourceDelegate: MediaDatasourceDelegate?
    @IBOutlet var collectionView :UICollectionView!
    @IBOutlet var exportButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        mediaDatasourceDelegate = MediaDatasourceDelegate()
        collectionView.dataSource = mediaDatasourceDelegate
        collectionView.delegate = mediaDatasourceDelegate
        
        
        if let flowLayout = collectionView.collectionViewLayout as? UICollectionViewFlowLayout {
            let width = (UIScreen.main.bounds.width / 2) - 15
            flowLayout.itemSize = CGSize(width: width, height: width)
            flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func buttonClicked(){
        let imagePicker = UIImagePickerController()
        imagePicker.mediaTypes = ["public.movie"]
        imagePicker.delegate = self
        present(imagePicker, animated: true, completion: nil)
    }
    
    @IBAction func exportClicked(){
    
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        print("Picked:\(info)")
 
        dismiss(animated: true, completion: nil)
        guard let url = info["UIImagePickerControllerMediaURL"] as? URL, info["UIImagePickerControllerMediaType"] as? String == "public.movie"  else {
            print("Error: \(info)")
            return
        }
        
        let movie = KMMBasicMedia(isVideo: true, path: url.absoluteString, duration: 0)
        
        movieCollection.append(movie)
        mediaDatasourceDelegate?.setItems(items: movieCollection)
        collectionView.reloadData()
        
        print("Selected media: \(movieCollection.count)")

    }

}

