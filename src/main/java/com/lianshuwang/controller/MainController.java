package com.lianshuwang.controller;

import com.lianshuwang.domin.Book;
import com.lianshuwang.domin.BookType;
import com.lianshuwang.helper.PageHelper;
import com.lianshuwang.helper.RankingBook;
import com.lianshuwang.service.BookService;
import com.lianshuwang.util.PropertyConfigurer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
//图书排序等
@Controller
public class MainController {

    private static final Log logger = LogFactory.getLog(MainController.class);

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/index")
    public String index(Model model) {
        logger.info("Welcome to Ebook!");

        String name = (String)PropertyConfigurer.getProperty("book_path");
        System.out.println("hello, " + name);
        //全部图书数
        int sumOfBooks = bookService.queryNumberOfBooks();
        model.addAttribute("sumOfBooks", sumOfBooks);

        List<Integer> everyTypeBooks;
        everyTypeBooks = bookService.queryNumberOfSomeTypeBooks();
        model.addAttribute("sumOfTypeBooks",everyTypeBooks);

        String maxUploadDate = bookService.getMaxUploadDate();
        model.addAttribute("maxUploadDate", maxUploadDate);

        List<RankingBook> rankingBooks;
        rankingBooks = bookService.queryByUploadedDate();
        model.addAttribute("rankingBooks",rankingBooks);
        List<RankingBook> rankingBooks1;
        rankingBooks1 = bookService.queryByDownloadTimes();
        model.addAttribute("rankingBooks1",rankingBooks1);
        PageHelper page=new PageHelper();
        page.setTotalRows(8);
        List<BookType> smallTypes;
        smallTypes = bookService.getSmallTypesOfBook("经典文学");
        List<Book> books = bookService.getLargeTypeBooks(smallTypes,page);
        model.addAttribute("books",books);
        smallTypes = bookService.getSmallTypesOfBook("通俗小说");
        List<Book> bookts = bookService.getLargeTypeBooks(smallTypes,page);
        model.addAttribute("bookts",bookts);
        smallTypes = bookService.getSmallTypesOfBook("计算机类");
        List<Book> booktjs = bookService.getLargeTypeBooks(smallTypes,page);
        model.addAttribute("booktjs",booktjs);
        smallTypes = bookService.getSmallTypesOfBook("杂志期刊");
        List<Book> booktqk = bookService.getLargeTypeBooks(smallTypes,page);
        model.addAttribute("booktqk",booktqk);
        return "main";
    }
}
