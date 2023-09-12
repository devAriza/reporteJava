 package vistas;

       
import config.Conexion;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
 

public class Reporte extends javax.swing.JFrame {
    
    Conexion cc = new Conexion();
    Connection cn = cc.conectar();
    DefaultTableModel modelo = new DefaultTableModel();
    ResultSet rs;
    TableRowSorter<TableModel> trs;
    String[] titulos = {"NoFlete", "Cliente", "Origen", "Destino", "Distancia", "Peso", "Volumen", "TipoViaje", "Costo"};
    String [][] datos;
    
    int xMouse, yMouse;
    
    public Reporte() {
        initComponents();
        //mostrarTabla("");
        consultarDatos();
        
    }
    
    private void consultarDatos(){
        int contador = 0;
        try {
            
            Statement stCont = cn.createStatement();
            ResultSet rsCont = stCont.executeQuery("SELECT COUNT(*) AS TotalRegistros\n" +
                                                    "FROM reportes AS r\n" +
                                                    "INNER JOIN fletes AS f ON f.idflete = r.idflete\n" +
                                                    "INNER JOIN clientes AS c ON c.idcliente = f.idcliente\n" +
                                                    "INNER JOIN viajes AS v ON v.idviaje = f.idviaje\n" +
                                                    "INNER JOIN origenfletes AS of ON of.idorigen = v.idorigen\n" +
                                                    "INNER JOIN destinofletes AS df ON df.iddestino = v.iddestino");
            if(rsCont.next()){
                contador = rsCont.getInt(1);
            }
            
            Statement st = cn.createStatement();
            rs = st.executeQuery("SELECT f.idflete AS NoFlete, c.nombre AS Cliente, of.estado AS Origen, df.estado AS Destino, v.distancia AS Distancia, f.peso AS Peso, f.volumen AS Volumen,\n" +
                                "IF(f.redondo = TRUE,'redondo','no redondo') AS TipoViaje,\n" +
                                "CASE \n" +
                                "	WHEN f.redondo = TRUE AND f.peso > 40\n" +
                                "		THEN\n" +
                                "		CONCAT('$',ROUND((((v.distancia * f.volumen) * 10) * 1.10),2))\n" +
                                "	WHEN f.redondo = FALSE AND f.peso > 40\n" +
                                "		THEN\n" +
                                "		CONCAT('$',ROUND((((v.distancia * f.volumen) * 10) * 1.20),2))\n" +
                                "	WHEN f.redondo = TRUE AND f.peso <= 40\n" +
                                "		THEN\n" +
                                "		CONCAT('$',ROUND(((v.distancia * f.volumen) * 10),2) - ROUND(((((v.distancia * f.volumen) * 10) * 10) / 100),2))\n" +
                                "	ELSE \n" +
                                "		CONCAT('$',ROUND(((v.distancia * f.volumen) * 10),2))\n" +
                                "END AS Costo\n" +
                                "FROM reportes AS r\n" +
                                "INNER JOIN fletes AS f ON f.idflete = r.idflete\n" +
                                "INNER JOIN clientes AS c ON c.idcliente = f.idcliente\n" +
                                "INNER JOIN viajes AS v ON v.idviaje = f.idviaje\n" +
                                "INNER JOIN origenfletes AS of ON of.idorigen = v.idorigen\n" +
                                "INNER JOIN destinofletes AS df ON df.iddestino = v.iddestino");
            
            int cont = 0;
            datos = new String[contador][9];
            while (rs.next()){         
                datos[cont][0] = rs.getString("NoFlete");
                datos[cont][1] = rs.getString("Cliente");
                datos[cont][2] = rs.getString("Origen");
                datos[cont][3] = rs.getString("Destino");
                datos[cont][4] = rs.getString("Distancia");
                datos[cont][5] = rs.getString("Peso");
                datos[cont][6] = rs.getString("Volumen");
                datos[cont][7] = rs.getString("TipoViaje");
                datos[cont][8] = rs.getString("Costo");                
                cont = cont + 1;
            }                       
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de consulta. Error: "+e);
        }
        
        modelo = new DefaultTableModel(datos,titulos){
            public boolean isCellEditable(int row, int column){
                return false;
            }                                
        };
        
        tabla.setModel(modelo);
        trs = new TableRowSorter<>(modelo);
        tabla.setRowSorter(trs);                    
        
    }
    
    private void consultaVariable(String var1, String campo){
        int valor = 0;
        int cont = 0;
        String aux = "" + txtCliente.getText();
        try {
            Statement stCont = cn.createStatement();
            rs = stCont.executeQuery("SELECT COUNT(*) AS TotalRegistros\n" +
                                                    "FROM reportes AS r\n" +
                                                    "INNER JOIN fletes AS f ON f.idflete = r.idflete\n" +
                                                    "INNER JOIN clientes AS c ON c.idcliente = f.idcliente\n" +
                                                    "INNER JOIN viajes AS v ON v.idviaje = f.idviaje\n" +
                                                    "INNER JOIN origenfletes AS of ON of.idorigen = v.idorigen\n" +
                                                    "INNER JOIN destinofletes AS df ON df.iddestino = v.iddestino\n" +
                                                    "WHERE " + campo + " LIKE'" + var1 + "%'");
            
            if(rs.next()){
                valor = rs.getInt(1);
            }
            
            datos = new String [valor][9];
            rs = stCont.executeQuery("SELECT f.idflete AS NoFlete, c.nombre AS Cliente, of.estado AS Origen, df.estado AS Destino, v.distancia AS Distancia, f.peso AS Peso, f.volumen AS Volumen,\n" +
                                    "IF(f.redondo = TRUE,'redondo','no redondo') AS TipoViaje,\n" +
                                    "CASE \n" +
                                    "	WHEN f.redondo = TRUE AND f.peso > 40\n" +
                                    "		THEN\n" +
                                    "		CONCAT('$',ROUND((((v.distancia * f.volumen) * 10) * 1.10),2))\n" +
                                    "	WHEN f.redondo = FALSE AND f.peso > 40\n" +
                                    "		THEN\n" +
                                    "		CONCAT('$',ROUND((((v.distancia * f.volumen) * 10) * 1.20),2))\n" +
                                    "	WHEN f.redondo = TRUE AND f.peso <= 40\n" +
                                    "		THEN\n" +
                                    "		CONCAT('$',ROUND(((v.distancia * f.volumen) * 10),2) - ROUND(((((v.distancia * f.volumen) * 10) * 10) / 100),2))\n" +
                                    "	ELSE \n" +
                                    "		CONCAT('$',ROUND(((v.distancia * f.volumen) * 10),2))\n" +
                                    "END AS Costo\n" +
                                    "FROM reportes AS r\n" +
                                    "INNER JOIN fletes AS f ON f.idflete = r.idflete\n" +
                                    "INNER JOIN clientes AS c ON c.idcliente = f.idcliente\n" +
                                    "INNER JOIN viajes AS v ON v.idviaje = f.idviaje\n" +
                                    "INNER JOIN origenfletes AS of ON of.idorigen = v.idorigen\n" +
                                    "INNER JOIN destinofletes AS df ON df.iddestino = v.iddestino\n" +
                                    "WHERE " + campo + " LIKE'" + var1 + "%' ");
            
            while(rs.next()){
                datos[cont][0] = rs.getString("NoFlete");
                datos[cont][1] = rs.getString("Cliente");
                datos[cont][2] = rs.getString("Origen");
                datos[cont][3] = rs.getString("Destino");
                datos[cont][4] = rs.getString("Distancia");
                datos[cont][5] = rs.getString("Peso");
                datos[cont][6] = rs.getString("Volumen");
                datos[cont][7] = rs.getString("TipoViaje");
                datos[cont][8] = rs.getString("Costo");                
                cont = cont + 1;
            }
            
            modelo = new DefaultTableModel(datos,titulos){
                public boolean isCellEditable(int row, int column){
                    return false;
                }                                
            };

            tabla.setModel(modelo);
            trs = new TableRowSorter<>(modelo);
            tabla.setRowSorter(trs);
            
            
            
            
        } catch (Exception e) {
            System.out.println("Fallo. Error: "+e);
        }
    }
    
    private void consultaEstados(String varnombre, String varorigen, String vardestino){
        
        int valor = 0;
        int cont = 0;
        String aux = "" + txtCliente.getText();
        try {
            Statement stCont = cn.createStatement();
            rs = stCont.executeQuery("SELECT COUNT(*) AS TotalRegistros\n" +
                                    "FROM reportes AS r\n" +
                                    "INNER JOIN fletes AS f ON f.idflete = r.idflete\n" +
                                    "INNER JOIN clientes AS c ON c.idcliente = f.idcliente\n" +
                                    "INNER JOIN viajes AS v ON v.idviaje = f.idviaje\n" +
                                    "INNER JOIN origenfletes AS of ON of.idorigen = v.idorigen\n" +
                                    "INNER JOIN destinofletes AS df ON df.iddestino = v.iddestino\n" +
                                    "WHERE (" + varnombre + " LIKE '" + txtCliente.getText() + "') OR (" + varorigen + " LIKE '" + cbOrigen.getSelectedItem() + "' AND " + vardestino + " LIKE '" + cbDestino.getSelectedItem() + "')");
            
            if(rs.next()){
                valor = rs.getInt(1);
            }
            
            datos = new String [valor][9];
            rs = stCont.executeQuery("SELECT f.idflete AS NoFlete, c.nombre AS Cliente, of.estado AS Origen, df.estado AS Destino, v.distancia AS Distancia, f.peso AS Peso, f.volumen AS Volumen,\n" +
                                    "IF(f.redondo = TRUE,'redondo','no redondo') AS TipoViaje,\n" +
                                    "CASE \n" +
                                    "	WHEN f.redondo = TRUE AND f.peso > 40\n" +
                                    "		THEN\n" +
                                    "		CONCAT('$',ROUND((((v.distancia * f.volumen) * 10) * 1.10),2))\n" +
                                    "	WHEN f.redondo = FALSE AND f.peso > 40\n" +
                                    "		THEN\n" +
                                    "		CONCAT('$',ROUND((((v.distancia * f.volumen) * 10) * 1.20),2))\n" +
                                    "	WHEN f.redondo = TRUE AND f.peso <= 40\n" +
                                    "		THEN\n" +
                                    "		CONCAT('$',ROUND(((v.distancia * f.volumen) * 10),2) - ROUND(((((v.distancia * f.volumen) * 10) * 10) / 100),2))\n" +
                                    "	ELSE \n" +
                                    "		CONCAT('$',ROUND(((v.distancia * f.volumen) * 10),2))\n" +
                                    "END AS Costo\n" +
                                    "FROM reportes AS r\n" +
                                    "INNER JOIN fletes AS f ON f.idflete = r.idflete\n" +
                                    "INNER JOIN clientes AS c ON c.idcliente = f.idcliente\n" +
                                    "INNER JOIN viajes AS v ON v.idviaje = f.idviaje\n" +
                                    "INNER JOIN origenfletes AS of ON of.idorigen = v.idorigen\n" +
                                    "INNER JOIN destinofletes AS df ON df.iddestino = v.iddestino\n" +
                                    "WHERE (" + varnombre + " LIKE '" + txtCliente.getText() + "') OR (" + varorigen + " LIKE '" + cbOrigen.getSelectedItem() + "' AND " + vardestino + " LIKE '" + cbDestino.getSelectedItem() + "')");
            
            while(rs.next()){
                datos[cont][0] = rs.getString("NoFlete");
                datos[cont][1] = rs.getString("Cliente");
                datos[cont][2] = rs.getString("Origen");
                datos[cont][3] = rs.getString("Destino");
                datos[cont][4] = rs.getString("Distancia");
                datos[cont][5] = rs.getString("Peso");
                datos[cont][6] = rs.getString("Volumen");
                datos[cont][7] = rs.getString("TipoViaje");
                datos[cont][8] = rs.getString("Costo");                
                cont = cont + 1;
            }
            
            modelo = new DefaultTableModel(datos,titulos){
                public boolean isCellEditable(int row, int column){
                    return false;
                }                                
            };

            tabla.setModel(modelo);
            trs = new TableRowSorter<>(modelo);
            tabla.setRowSorter(trs);
            
            
            
            
        } catch (Exception e) {
            System.out.println("Fallo. Error: "+e);
        }
        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        ReporteT = new javax.swing.JLabel();
        iconoAzteca = new javax.swing.JLabel();
        bordebg = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        exitBtn = new javax.swing.JPanel();
        exitTxt = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        tFiltros = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        tCliente = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        tOrigen = new javax.swing.JLabel();
        tDestino = new javax.swing.JLabel();
        cbOrigen = new javax.swing.JComboBox<>();
        cbDestino = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ReporteT.setFont(new java.awt.Font("Lucida Sans", 1, 36)); // NOI18N
        ReporteT.setForeground(new java.awt.Color(255, 255, 255));
        ReporteT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ReporteT.setText("Reporte");
        bg.add(ReporteT, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 180, 40));

        iconoAzteca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoAzP.png"))); // NOI18N
        bg.add(iconoAzteca, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, -1, -1));

        bordebg.setFont(new java.awt.Font("Fira Code", 0, 12)); // NOI18N
        bordebg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bordebg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bordeL.png"))); // NOI18N
        bordebg.setText("Reporte");
        bg.add(bordebg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 500));

        header.setBackground(new java.awt.Color(255, 255, 255));
        header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerMouseDragged(evt);
            }
        });
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerMousePressed(evt);
            }
        });

        exitBtn.setBackground(new java.awt.Color(255, 255, 255));
        exitBtn.setForeground(new java.awt.Color(255, 255, 255));

        exitTxt.setFont(new java.awt.Font("Lucida Sans", 1, 24)); // NOI18N
        exitTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exitTxt.setText("X");
        exitTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exitTxt.setPreferredSize(new java.awt.Dimension(40, 40));
        exitTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout exitBtnLayout = new javax.swing.GroupLayout(exitBtn);
        exitBtn.setLayout(exitBtnLayout);
        exitBtnLayout.setHorizontalGroup(
            exitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exitTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
        );
        exitBtnLayout.setVerticalGroup(
            exitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exitBtnLayout.createSequentialGroup()
                .addComponent(exitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addGap(0, 828, Short.MAX_VALUE)
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bg.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 880, 40));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NoFlete", "Cliente", "Origen", "Destino", "Distancia", "Peso", "Volumen", "TipoViaje", "Costo"
            }
        ));
        jScrollPane1.setViewportView(tabla);

        bg.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 660, 190));

        tFiltros.setFont(new java.awt.Font("Lucida Sans", 1, 22)); // NOI18N
        tFiltros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tFiltros.setText("Filtros");
        bg.add(tFiltros, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, 80, -1));

        txtCliente.setForeground(new java.awt.Color(204, 204, 204));
        txtCliente.setText("Ingrese nombre de cliente");
        txtCliente.setBorder(null);
        txtCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtClienteMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtClienteMousePressed(evt);
            }
        });
        txtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteActionPerformed(evt);
            }
        });
        txtCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtClienteKeyReleased(evt);
            }
        });
        bg.add(txtCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 170, 30));

        tCliente.setFont(new java.awt.Font("Lucida Sans", 1, 14)); // NOI18N
        tCliente.setText("Cliente");
        bg.add(tCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, -1, -1));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        bg.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 190, 170, 30));

        tOrigen.setFont(new java.awt.Font("Lucida Sans", 1, 14)); // NOI18N
        tOrigen.setText("Origen");
        bg.add(tOrigen, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 140, -1, -1));

        tDestino.setFont(new java.awt.Font("Lucida Sans", 1, 14)); // NOI18N
        tDestino.setText("Destino");
        bg.add(tDestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 140, -1, -1));

        cbOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"", "Puebla", "Veracruz", "DF", "Tlaxcala" }));
        cbOrigen.setBorder(null);
        cbOrigen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbOrigenMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbOrigenMousePressed(evt);
            }
        });
        cbOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOrigenActionPerformed(evt);
            }
        });
        bg.add(cbOrigen, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 160, 170, 30));

        cbDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"", "Puebla", "Veracruz", "DF", "Tlaxcala" }));
        cbDestino.setBorder(null);
        cbDestino.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbDestinoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbDestinoMousePressed(evt);
            }
        });
        cbDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDestinoActionPerformed(evt);
            }
        });
        bg.add(cbDestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 160, 170, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void headerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_headerMousePressed

    private void headerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_headerMouseDragged

    private void exitTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitTxtMouseClicked

    private void exitTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseEntered
        exitBtn.setBackground(Color.red);
        exitTxt.setForeground(Color.white);
    }//GEN-LAST:event_exitTxtMouseEntered

    private void exitTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseExited
        exitBtn.setBackground(Color.white);
        exitTxt.setForeground(Color.black);
    }//GEN-LAST:event_exitTxtMouseExited

    private void txtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteActionPerformed
        
    }//GEN-LAST:event_txtClienteActionPerformed

    private void txtClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtClienteMousePressed
        if(txtCliente.getText().equals("Ingrese nombre de cliente")){            
            txtCliente.setText("");
            txtCliente.setForeground(Color.black);                        
        }      
    }//GEN-LAST:event_txtClienteMousePressed

    private void cbOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrigenActionPerformed
        //txtCliente.setForeground(Color.gray);
        //txtCliente.setText("Ingrese nombre de cliente");
        if(cbDestino.getSelectedIndex() == 1 || cbDestino.getSelectedIndex() == 2 || cbDestino.getSelectedIndex() == 3 || cbDestino.getSelectedIndex() == 4){
            String camponombre = "c.nombre";
            String campoorigen = "of.estado";
            String campodestino = "df.estado";
            consultaEstados(camponombre, campoorigen, campodestino);
        }else if(cbDestino.getItemCount() == 0){
            String cbDesti = cbOrigen.getSelectedItem().toString();
            String campoD = "of.estado";
            consultaVariable(cbDesti, campoD);
        }else {
            String cbDesti = cbOrigen.getSelectedItem().toString();
            String campoD = "of.estado";
            consultaVariable(cbDesti, campoD);
        }
        
        
    }//GEN-LAST:event_cbOrigenActionPerformed

    private void cbDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDestinoActionPerformed
        //txtCliente.setForeground(Color.gray);
        //txtCliente.setText("Ingrese nombre de cliente");
        if(cbOrigen.getSelectedIndex() == 1 || cbOrigen.getSelectedIndex() == 2 || cbOrigen.getSelectedIndex() == 3 || cbOrigen.getSelectedIndex() == 4){
            String camponombre = "c.nombre";
            String campoorigen = "of.estado";
            String campodestino = "df.estado";
            consultaEstados(camponombre, campoorigen, campodestino);
        }else if(cbOrigen.getItemCount() == 0 ){
            String cbdesti = cbOrigen.getSelectedItem().toString();
            String campoD = "df.estado";
            consultaVariable(cbdesti, campoD);
        }else{
            String cbDesti = cbDestino.getSelectedItem().toString();
            String campoD = "df.estado";
            consultaVariable(cbDesti, campoD);
        }
        
                
    }//GEN-LAST:event_cbDestinoActionPerformed

    private void txtClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClienteKeyReleased
        String textoCliente = txtCliente.getText();
        String campoC = "c.nombre";        
        String texto = txtCliente.getText();
        if(texto.isEmpty()){
            cbOrigen.enable(true);
            cbDestino.enable(true);            
        }else{            
            //cbOrigen.enable(false);
            //cbDestino.enable(false);           
            cbOrigen.setSelectedIndex(0);
            cbDestino.setSelectedIndex(0);
        }
        consultaVariable(textoCliente, campoC);
        
    }//GEN-LAST:event_txtClienteKeyReleased

    private void cbOrigenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbOrigenMouseClicked
        txtCliente.setText("");
    }//GEN-LAST:event_cbOrigenMouseClicked

    private void cbDestinoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbDestinoMouseClicked
        txtCliente.setText("");
    }//GEN-LAST:event_cbDestinoMouseClicked

    private void cbOrigenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbOrigenMousePressed
        
    }//GEN-LAST:event_cbOrigenMousePressed

    private void cbDestinoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbDestinoMousePressed
        
    }//GEN-LAST:event_cbDestinoMousePressed

    private void txtClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtClienteMouseClicked
        if(cbOrigen.getItemCount() > 0 || cbDestino.getItemCount() > 0){
            cbOrigen.setSelectedIndex(0);
            cbDestino.setSelectedIndex(0);
            txtCliente.setText("");
        }
    }//GEN-LAST:event_txtClienteMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reporte().setVisible(true);
            }
        });
    }
    
    
     
   
    /*void consultar(){
        String sql = "SELECT f.idflete AS NoFlete, c.nombre AS Cliente, of.estado AS Origen, df.estado AS Destino, v.distancia AS Distancia, f.peso AS Peso, f.volumen AS Volumen,\n" +
                    "IF(f.redondo = TRUE,'redondo','no redondo') AS TipoViaje,\n" +
                    "CASE \n" +
                    "	WHEN f.redondo = TRUE AND f.peso > 40\n" +
                    "		THEN\n" +
                    "		CONCAT('$',ROUND((((v.distancia * f.volumen) * 10) * 1.10),2))\n" +
                    "	WHEN f.redondo = FALSE AND f.peso > 40\n" +
                    "		THEN\n" +
                    "		CONCAT('$',ROUND((((v.distancia * f.volumen) * 10) * 1.20),2))\n" +
                    "	WHEN f.redondo = TRUE AND f.peso <= 40\n" +
                    "		THEN\n" +
                    "		CONCAT('$',ROUND(((v.distancia * f.volumen) * 10),2) - ROUND(((((v.distancia * f.volumen) * 10) * 10) / 100),2))\n" +
                    "	ELSE \n" +
                    "		CONCAT('$',ROUND(((v.distancia * f.volumen) * 10),2))\n" +
                    "END AS Costo\n" +
                    "FROM reportes AS r\n" +
                    "INNER JOIN fletes AS f ON f.idflete = r.idflete\n" +
                    "INNER JOIN clientes AS c ON c.idcliente = f.idcliente\n" +
                    "INNER JOIN viajes AS v ON v.idviaje = f.idviaje\n" +
                    "INNER JOIN origenfletes AS of ON of.idorigen = v.idorigen\n" +
                    "INNER JOIN destinofletes AS df ON df.iddestino = v.iddestino\n";
                    //"WHERE c.nombre LIKE '%"+valor+"%'";
        
        try {
            conet = con1.getConnection();
            st = conet.createStatement();
            rs = st.executeQuery(sql);
            Object[] reporte = new Object[9];
            modelo = (DefaultTableModel) tabla.getModel();
            while(rs.next()){
                reporte [0] = rs.getInt("NoFlete");
                reporte [1] = rs.getString("Cliente");
                reporte [2] = rs.getString("Origen");
                reporte [3] = rs.getString("Destino");
                reporte [4] = rs.getDouble("Distancia");
                reporte [5] = rs.getDouble("Peso");
                reporte [6] = rs.getDouble("Volumen");
                reporte [7] = rs.getString("TipoViaje");
                reporte [8] = rs.getString("Costo");
                
                modelo.addRow(reporte);
            }
            
            tabla.setModel(modelo);
            
            
        } catch (SQLException e) {
            System.err.println("Error en tabla");
            JOptionPane.showMessageDialog(null,"Error en tabla: "+e);
        }
        
        
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ReporteT;
    private javax.swing.JPanel bg;
    private javax.swing.JLabel bordebg;
    private javax.swing.JComboBox<String> cbDestino;
    private javax.swing.JComboBox<String> cbOrigen;
    private javax.swing.JPanel exitBtn;
    private javax.swing.JLabel exitTxt;
    private javax.swing.JPanel header;
    private javax.swing.JLabel iconoAzteca;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel tCliente;
    private javax.swing.JLabel tDestino;
    private javax.swing.JLabel tFiltros;
    private javax.swing.JLabel tOrigen;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtCliente;
    // End of variables declaration//GEN-END:variables



}
