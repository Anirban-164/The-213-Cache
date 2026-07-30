/*
A reporting module that always does the same three things in the same order: fetch the data -> build the body -> wrap it in a header/footer
--> but needs to emit either a PDF or an HTML report.
--> The header/footer and the overall order are identical for both, so they live once in the abstract class; only buildBody() is overridden.
*/

abstract class ReportGenerator {
    // Template method: fixed order, subclasses cannot reorder it
    public final void generate() {
        fetchData();
        buildHeader();
        buildBody(); // the only step that varies
        buildFooter();
    }

    void fetchData() {
        System.out.println("Fetching data from DB...");
    }

    void buildHeader() {
        System.out.println("Building standard report header");
    }

    void buildFooter() {
        System.out.println("Building standard report footer");
    }

    // Abstract hook — each subclass provides its own rendering
    abstract void buildBody();
}

class PdfReportGenerator extends ReportGenerator {
    void buildBody() {
        System.out.println("Formatting body as PDF tables");
    }
}

class HtmlReportGenerator extends ReportGenerator {
    void buildBody() {
        System.out.println("Formatting body as HTML <div> elements");
    }
}

class CsvReportGenerator extends ReportGenerator {
    void buildBody() {
        System.out.println("Formatting body as comma-separated values");
    }
}

public class DocReader {
    public static void main(String[] args) {
        ReportGenerator pdf  = new PdfReportGenerator();
        ReportGenerator html = new HtmlReportGenerator();
        ReportGenerator csv  = new CsvReportGenerator();

        System.out.println("--- PDF report ---");
        pdf.generate();

        System.out.println("\n--- HTML report ---");
        html.generate();

        System.out.println("\n--- CSV report ---");
        csv.generate();
    }
}