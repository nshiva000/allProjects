//
//  ViewController.swift
//  KioLearn
//
//  Created by Shiva Prasad Reddy on 29/08/19.
//  Copyright © 2019 Kio learn. All rights reserved.
//

import UIKit
import  WebKit

class ViewController: UIViewController {
    
    var webView: WKWebView!
    //MARK: - Variables
    var urlString: String?
    
    var progressView: UIProgressView!
    
    
    

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        progressView = UIProgressView(progressViewStyle: .bar)
        progressView = UIProgressView(frame: CGRect(x: 0, y: 47, width: view.frame.size.width, height: 2))
        progressView.sizeToFit()
        
       
        urlString = "https://live.kiolearn.com"
        
        webView = WKWebView(frame: CGRect(x: 0, y: 47, width: view.frame.size.width, height: view.frame.size.height-47))
        //webView.navigationDelegate = self
        webView.allowsBackForwardNavigationGestures = true
        self.view.addSubview(webView)
        if let url = URL(string: urlString ?? "") {
            webView.load(URLRequest(url: url))
            webView.addObserver(self, forKeyPath: #keyPath(WKWebView.estimatedProgress), options: .new, context: nil)
        }
        
        view.addSubview(progressView)
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        if keyPath == "estimatedProgress" {
            self.progressView.progress = Float(webView.estimatedProgress)
        }
    }
    
    
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
    
}

