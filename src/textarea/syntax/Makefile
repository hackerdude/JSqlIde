#
# Makefile for org.gjt.sp.jedit.syntax package
#
# It compiles files in the above package.
#

classes=org/gjt/sp/jedit/syntax/BatchFileTokenMarker.class \
	org/gjt/sp/jedit/syntax/CCTokenMarker.class \
	org/gjt/sp/jedit/syntax/CTokenMarker.class \
	org/gjt/sp/jedit/syntax/HTMLTokenMarker.class \
	org/gjt/sp/jedit/syntax/IDLTokenMarker.class \
	org/gjt/sp/jedit/syntax/JavaScriptTokenMarker.class \
	org/gjt/sp/jedit/syntax/JavaTokenMarker.class \
	org/gjt/sp/jedit/syntax/KeywordMap.class \
	org/gjt/sp/jedit/syntax/MakefileTokenMarker.class \
	org/gjt/sp/jedit/syntax/PatchTokenMarker.class \
	org/gjt/sp/jedit/syntax/PerlTokenMarker.class \
	org/gjt/sp/jedit/syntax/PropsTokenMarker.class \
	org/gjt/sp/jedit/syntax/PythonTokenMarker.class \
	org/gjt/sp/jedit/syntax/ShellScriptTokenMarker.class \
	org/gjt/sp/jedit/syntax/SQLTokenMarker.class \
	org/gjt/sp/jedit/syntax/SyntaxDocument.class \
	org/gjt/sp/jedit/syntax/SyntaxStyle.class \
	org/gjt/sp/jedit/syntax/SyntaxUtilities.class \
	org/gjt/sp/jedit/syntax/TeXTokenMarker.class \
	org/gjt/sp/jedit/syntax/Token.class \
	org/gjt/sp/jedit/syntax/TokenMarker.class \
	org/gjt/sp/jedit/syntax/TSQLTokenMarker.class

others='org/gjt/sp/jedit/syntax/KeywordMap$$Keyword.class' \
	'org/gjt/sp/jedit/syntax/TokenMarker$$LineInfo.class'

all: $(classes)
	echo $(classes) $(others) >> .resources

include Rules.make
