#
# Makefile for org.gjt.sp.jedit.textarea package
#
# It compiles files in the above package.
#

classes=org/gjt/sp/jedit/textarea/DefaultInputHandler.class \
	org/gjt/sp/jedit/textarea/InputHandler.class \
	org/gjt/sp/jedit/textarea/JEditTextArea.class \
	org/gjt/sp/jedit/textarea/TextAreaDefaults.class \
	org/gjt/sp/jedit/textarea/TextAreaPainter.class \
	org/gjt/sp/jedit/textarea/TextUtilities.class

others='org/gjt/sp/jedit/textarea/DefaultInputHandler$$backspace.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$delete.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$end.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$home.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$insert_break.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$insert_tab.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$next_char.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$next_line.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$next_page.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$next_word.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$overwrite.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$prev_char.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$prev_line.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$prev_page.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$prev_word.class' \
	'org/gjt/sp/jedit/textarea/DefaultInputHandler$$toggle_rect.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$1.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$AdjustHandler.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$CaretBlinker.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$DocumentHandler.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$DragHandler.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$ComponentHandler.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$FocusHandler.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$MouseHandler.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$MutableCaretEvent.class' \
	'org/gjt/sp/jedit/textarea/JEditTextArea$$ScrollLayout.class'

all: $(classes)
	echo $(classes) $(others) >> .resources

include Rules.make
