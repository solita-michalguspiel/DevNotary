//
//  ShareNoteSheet.swift
//  iosApp
//
//  Created by Michal Guspiel on 6.6.2022.
//

import SwiftUI
import shared

class ShareNoteSheetObject : ObservableObject{
  
    let viewModel = iosDI().getNotesDetailViewModel()
    
    var listeners : [Closeable] = []
    
    @Published var sharingResponse : Any = ResponseEmpty.self
    
    
    func start(){
    let noteSharingStateListener = self.viewModel.watch(viewModel.noteSharingState,block : { response in
        self.sharingResponse = response!
        print(self.sharingResponse)
    })
        listeners.append(noteSharingStateListener)
    }
    func stop(){
        listeners.forEach{listener in
            listener.close()
        }
    }
    
    @Published var email = ""{
        didSet{
            viewModel.changeAnotherUserEmailAddress(newEmailAddress: String(email))
        }
    }
}
struct ShareNoteSheet : View{
    @Environment(\.dismiss) var dismiss
    @StateObject var stateObject = ShareNoteSheetObject()
    
    var body: some View{
        VStack{
            Text("Share note").font(.title)
            TextField("User e-mail address",text: $stateObject.email)
                .textInputAutocapitalization(.never)
                .disableAutocorrection(true)
                .padding(.horizontal, 30.0)
                .textFieldStyle(RoundedBorderTextFieldStyle())
            
            
            switch(stateObject.sharingResponse as Any){
            case _ as ResponseLoading:
                ProgressView()
            case _ as ResponseError:
                let error = stateObject.sharingResponse as! ResponseError
                Text(error.message)
                    .foregroundColor(.red)
                    .font(.caption)
            case _ as ResponseSuccess<AnyObject>:
                Text("Note shared successfully!")
                    .onAppear{
                        print("Success!!!!")
                    }
            default:
                Text("").onAppear{
                    print("Empty text appeared because of : \(stateObject.sharingResponse)")
                }
            }
            
            CustomBorderedButton(text: "Share"){
                stateObject.viewModel.shareNote()
            }
        }.onAppear{stateObject.start()}
            .onDisappear{
                stateObject.stop()
                stateObject.viewModel.restartNoteSharingState()
            }
    }
}
