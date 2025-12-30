//package com.next.engine.util;
//
//import com.sun.source.tree.IdentifierTree;
//import com.sun.source.util.TreePath;
//import com.sun.source.util.TreePathScanner;
//import com.sun.source.util.Trees;
//
//import javax.annotation.processing.*;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//import java.util.HashSet;
//import java.util.Set;
//
//@SupportedSourceVersion(SourceVersion.RELEASE_25)
//@SupportedAnnotationTypes("com.next.engine.util.Experimental")
//public class ExperimentalProcessor extends AbstractProcessor {
//
//    private Trees trees;
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        Set<Element> experimentalElements = new HashSet<>(roundEnv.getElementsAnnotatedWith(Experimental.class));
//        for (Element root : roundEnv.getRootElements()) {
//            TreePath path = trees.getPath(root);
//            new TreePathScanner<Void, Void>() {
//                @Override
//                public Void visitIdentifier(IdentifierTree node, Void unused) {
//                    Element e = trees.getElement(getCurrentPath());
//                    Experimental exp = e.getAnnotation(Experimental.class);
//                    if (exp == null) return super.visitIdentifier(node, unused);
//                    var diagnostic = exp.level() == Experimental.Level.ERROR ?
//                            Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;
//                    if (experimentalElements.contains(e)) {
//                        processingEnv.getMessager().printMessage(
//                                diagnostic,
//                                "Usage of experimental API: " + e.getSimpleName(),
//                                e
//                        );
//                    }
//                    return super.visitIdentifier(node, unused);
//                }
//            }.scan(path, null);
//        }
//
//        return false;
//    }
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        trees = Trees.instance(processingEnv);
//    }
//}
