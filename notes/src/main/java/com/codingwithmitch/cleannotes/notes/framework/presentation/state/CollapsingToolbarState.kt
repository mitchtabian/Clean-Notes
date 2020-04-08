package com.codingwithmitch.cleannotes.notes.framework.presentation.state


sealed class CollapsingToolbarState{

    class Collapsed: CollapsingToolbarState(){

        override fun toString(): String {
            return "Collapsed"
        }
    }

    class Expanded: CollapsingToolbarState(){

        override fun toString(): String {
            return "Expanded"
        }
    }
}
























