//
//  LocalNoteView.swift
//  iosApp
//
//  Created by Michal Guspiel on 1.6.2022.
//

import SwiftUI
import shared


class LocaLNoteViewHelper : ObservableObject{
    
    
    var notesViewModel = iosDI().getNotesViewModel()

    @Published var chosenColor = ""
    
    @Published var title = ""
    
    @Published var content = ""
    
    @Published var date = ""
    
    @Published var shouldPopBackStack : Bool = false


    init(){
        print("Local note view helper init")
        start()
    }
    
    func start(){
        
        self.notesViewModel.noteColor.watch(block : { newColor in
            self.chosenColor = newColor! as String
        })
        self.notesViewModel.titleInput.watch(block : {title in
            self.title = title! as String
        })
        self.notesViewModel.contentInput.watch(block : {content in
            self.content = content! as String
        })
        
        self.notesViewModel.noteModificationStatus.watch(block : { response in
            if(response is ResponseSuccess){
                let opResponse = response as! ResponseSuccess<shared.Operation>
                print(opResponse.description())
                switch (opResponse.data){
                case _ as shared.Operation.Delete:
                    self.shouldPopBackStack = true
                    //self.notesViewModel.resetNoteModificationStatus()

                default :
                    print("Default")
                }
            }
        })
        
    }
    
}

struct LocalNoteView: View {
        
    @ObservedObject var stateObject : LocaLNoteViewHelper = LocaLNoteViewHelper()
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>

    let note: Note
    
    init(note: Note){
        self.note = note
        print("preparing the screen with note : ")
        print(note.description())
        stateObject.notesViewModel.prepareNoteScreen(note: note)
    }
    
    var body: some View {
        VStack{
            ZStack{
                RoundedRectangle(cornerRadius: 20,style: .continuous)
                    .fill(NoteColor.init(color: stateObject.chosenColor).getColor())
                
                GeometryReader{ geo in
                    VStack{
                        TextField("Note title",text: .constant(stateObject.title))
                            .font(.title)
                            .padding(.horizontal)
                            .padding(.vertical,10)
                        Divider()
                        TextEditor(text : .constant(stateObject.content))
                            .background(.clear)
                            .frame(height: geo.size.height * 0.7)
                            .textFieldStyle(PlainTextFieldStyle())
                            .padding(.horizontal,10)
                        

                    }
                }
            }.padding()
            HStack{
                Spacer()
                if(isLocal()){
                    CustomBorderedButton(text : "New note"){
                        print("New note!")
                    }.padding(.horizontal,3)
                    CustomBorderedButton(text : "Delete note"){
                        stateObject.notesViewModel.deleteNote()
                    }.padding(.horizontal,3)
                    CustomBorderedButton(text  : "Edit note"){
                        print("Edit note!")
                    }.padding(.horizontal,3)
                }
                else{
                    CustomBorderedButton(text : "Delete note"){
                        print("New note!")
                    }.padding(.horizontal,3)
                    CustomBorderedButton(text : "Save note locally"){
                        print("New note!")
                    }.padding(.horizontal,3)
                }
            }.padding()
        }.background(Color.background)
        
        if stateObject.shouldPopBackStack {
            Text("").onAppear(){
                    presentationMode.wrappedValue.dismiss()
                    stateObject.notesViewModel.resetNoteModificationStatus()
                }
            }
    }
    
    func isLocal() -> Bool{
        return note.ownerUserId == nil
    }
    
}

struct LocalNoteView_Previews: PreviewProvider {
    
    static let testNote = Note.init(noteId: "testID", ownerUserId: "59017250192", title:"Database plan", content: "Some random SQL database plan, loreum ipseum bla la test loreum", dateTime: "2015.12.30 16:35", color: "blue")
    
    static var previews: some View {
        LocalNoteView(note: testNote)
    }
}
