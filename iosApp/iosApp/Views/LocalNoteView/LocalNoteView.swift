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

    @Published var displayedNote = Note.init(noteId: "", ownerUserId: nil, title: "", content: "", dateTime: "", color: "")
        
    @Published var shouldPopBackStack : Bool = false

    init(){
        start()
    }
    
    func start(){
        
        self.notesViewModel.watch(notesViewModel.displayedNote, block: { note in
            self.displayedNote = note as! Note
        })
        self.notesViewModel.watch(notesViewModel.noteModificationStatus, block: { response in
            if(response is ResponseSuccess<AnyObject>){
                let opResponse = response as! ResponseSuccess<shared.Operation>
                print(opResponse.description())
                switch (opResponse.data){
                case _ as shared.Operation.Delete:
                    self.shouldPopBackStack = true
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
        stateObject.notesViewModel.prepareNoteScreen(note: note)

    }
    
    var body: some View {
        VStack{
            
            ZStack{
                RoundedRectangle(cornerRadius: 20,style: .continuous)
                    .fill(NoteColor.init(color: stateObject.displayedNote.color).getColor())
                
                GeometryReader{ geo in
                    VStack{
                        TextField("Note title",text: .constant(stateObject.displayedNote.title))
                            .font(.title)
                            .padding(.horizontal)
                            .padding(.vertical,10)
                        Divider()
                        TextEditor(text : .constant(stateObject.displayedNote.content))
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
            .navigationBarBackButtonHidden(true)
            .toolbar(){
                ToolbarItem(placement: .navigationBarLeading){
                NavigationLink(destination: MainView(selectedTab: 2)){
                    Image(systemName: "arrow.left")
                }
            }
            }
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
