package badnieces.entities.compositor.document;

import badnieces.entities.strategies.merge.MergeOverlapPair;
import badnieces.entities.strategies.search.NextMaximallyOverlappingPair;
import badnieces.interfaces.strategy.merge.StringMergeStrategy;
import badnieces.interfaces.strategy.search.StringsListSearchStrategy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentCompositorTest {

    @Test
    public void recursiveMergeValid() {
        StringsListSearchStrategy stringsListSearchStrategy = new NextMaximallyOverlappingPair();
        StringMergeStrategy mergeStrategy = new MergeOverlapPair();
        String input = "O draconia;conian devil! Oh la;h lame sa;saint! ";
        // Test 1.
        String[] fragments = input.split(";");
        DocumentCompositor documentCompositor = new DocumentCompositor(stringsListSearchStrategy, mergeStrategy, fragments);
        documentCompositor.recursiveMerge();
        assertEquals("O draconian devil! Oh lame saint! ", documentCompositor.getToString());

        // Test 2.
        input = "m quaerat voluptatem.;pora incidunt ut labore et d;, consectetur, adipisci velit;olore magnam aliqua;idunt ut labore et dolore magn;uptatem.;i dolorem ipsum qu;iquam quaerat vol;psum quia dolor sit amet, consectetur, a;ia dolor sit amet, conse;squam est, qui do;Neque porro quisquam est, qu;aerat voluptatem.;m eius modi tem;Neque porro qui;, sed quia non numquam ei;lorem ipsum quia dolor sit amet;ctetur, adipisci velit, sed quia non numq;unt ut labore et dolore magnam aliquam qu;dipisci velit, sed quia non numqua;us modi tempora incid;Neque porro quisquam est, qui dolorem i;uam eius modi tem;pora inc;am al";
        fragments = input.split(";");
        documentCompositor = new DocumentCompositor(stringsListSearchStrategy, mergeStrategy, fragments);
        documentCompositor.recursiveMerge();
        assertEquals("Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.", documentCompositor.getToString());
    }
}