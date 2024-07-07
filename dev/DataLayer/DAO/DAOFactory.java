package dev.DataLayer.DAO;public class DAOFactory {
    private static ItemDAO itemDAO;
    private static DiscountNoteDAO discountNoteDAO;
    private static ContactDAO contactDAO;
    private static SupplierDAO supplierDAO;

    public static ItemDAO getItemDAO() {
        return itemDAO;
    }

    public static void setItemDAO(ItemDAO itemDAO) {
        DAOFactory.itemDAO = itemDAO;
    }

    public static DiscountNoteDAO getDiscountNoteDAO() {
        return discountNoteDAO;
    }

    public static void setDiscountNoteDAO(DiscountNoteDAO discountNoteDAO) {
        DAOFactory.discountNoteDAO = discountNoteDAO;
    }

    public static ContactDAO getContactDAO() {
        return contactDAO;
    }

    public static void setContactDAO(ContactDAO contactDAO) {
        DAOFactory.contactDAO = contactDAO;
    }

    public static SupplierDAO getSupplierDAO() {
        return supplierDAO;
    }

    public static void setSupplierDAO(SupplierDAO supplierDAO) {
        DAOFactory.supplierDAO = supplierDAO;
    }
}
