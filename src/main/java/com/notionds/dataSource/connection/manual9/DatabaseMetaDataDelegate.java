package com.notionds.dataSource.connection.manual9;

import com.notionds.dataSource.connection.ConnectionMember_I;
import com.notionds.dataSource.connection.State;

import java.sql.*;
import java.time.Instant;
import java.util.UUID;

public class DatabaseMetaDataDelegate<DM extends NotionWrapperManual9> implements DatabaseMetaData, ConnectionMember_I {
    
    private final NotionWrapperManual9 notionWrapper;
    private final DatabaseMetaData delegate;
    private final Instant startTime;
    private State state = State.Open;
    

    public DatabaseMetaDataDelegate(NotionWrapperManual9 notionWrapper, DatabaseMetaData delegate) {
        this.notionWrapper = notionWrapper;
        this.delegate = delegate;
        this.startTime = Instant.now();
    }
    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }
    @Override
    public void close() throws SQLException {
        this.notionWrapper.close(this);
    }
    @Override
    public void closeDelegate() throws SQLException {
        // no operation needed
    }

    @Override
    public Instant getCreateInstant() {
        return this.startTime;
    }

    @Override
    public UUID getConnectionId() {
        return this.notionWrapper.getConnectionId();
    }

    @Override
    public boolean allProceduresAreCallable() throws SQLException {
        try {
            return delegate.allProceduresAreCallable();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean allTablesAreSelectable() throws SQLException {
        try {
            return delegate.allTablesAreSelectable();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getURL() throws SQLException {
        try {
            return delegate.getURL();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getUserName() throws SQLException {
        try {
            return delegate.getUserName();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        try {
            return delegate.isReadOnly();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
        try {
            return delegate.nullsAreSortedHigh();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean nullsAreSortedLow() throws SQLException {
        try {
            return delegate.nullsAreSortedLow();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
        try {
            return delegate.nullsAreSortedAtStart();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
        try {
            return delegate.nullsAreSortedAtEnd();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        try {
            return delegate.getDatabaseProductName();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        try {
            return delegate.getDatabaseProductVersion();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getDriverName() throws SQLException {
        try {
            return delegate.getDriverName();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getDriverVersion() throws SQLException {
        try {
            return delegate.getDriverVersion();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getDriverMajorVersion() {
        return delegate.getDriverMajorVersion();
    }

    @Override
    public int getDriverMinorVersion() {
        return delegate.getDriverMinorVersion();
    }

    @Override
    public boolean usesLocalFiles() throws SQLException {
        try {
            return delegate.usesLocalFiles();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
        try {
            return delegate.usesLocalFilePerTable();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        try {
            return delegate.supportsMixedCaseIdentifiers();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        try {
            return delegate.storesUpperCaseIdentifiers();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        try {
            return delegate.storesLowerCaseIdentifiers();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        try {
            return delegate.storesMixedCaseIdentifiers();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        try {
            return delegate.supportsMixedCaseQuotedIdentifiers();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        try {
            return delegate.storesUpperCaseQuotedIdentifiers();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        try {
            return delegate.storesLowerCaseQuotedIdentifiers();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        try {
            return delegate.storesMixedCaseQuotedIdentifiers();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getIdentifierQuoteString() throws SQLException {
        try {
            return delegate.getIdentifierQuoteString();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getSQLKeywords() throws SQLException {
        try {
            return delegate.getSQLKeywords();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getNumericFunctions() throws SQLException {
        try {
            return delegate.getNumericFunctions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getStringFunctions() throws SQLException {
        try {
            return delegate.getStringFunctions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getSystemFunctions() throws SQLException {
        try {
            return delegate.getSystemFunctions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getTimeDateFunctions() throws SQLException {
        try {
            return delegate.getTimeDateFunctions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getSearchStringEscape() throws SQLException {
        try {
            return delegate.getSearchStringEscape();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getExtraNameCharacters() throws SQLException {
        try {
            return delegate.getExtraNameCharacters();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        try {
            return delegate.supportsAlterTableWithAddColumn();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        try {
            return delegate.supportsAlterTableWithDropColumn();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsColumnAliasing() throws SQLException {
        try {
            return delegate.supportsColumnAliasing();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
        try {
            return delegate.nullPlusNonNullIsNull();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsConvert() throws SQLException {
        try {
            return delegate.supportsConvert();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        try {
            return delegate.supportsConvert(fromType, toType);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
        try {
            return delegate.supportsTableCorrelationNames();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        try {
            return delegate.supportsDifferentTableCorrelationNames();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        try {
            return delegate.supportsExpressionsInOrderBy();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
        try {
            return delegate.supportsOrderByUnrelated();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsGroupBy() throws SQLException {
        try {
            return delegate.supportsGroupBy();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
        try {
            return delegate.supportsGroupByUnrelated();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        try {
            return delegate.supportsGroupByBeyondSelect();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
        try {
            return delegate.supportsLikeEscapeClause();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
        try {
            return delegate.supportsMultipleResultSets();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
        try {
            return delegate.supportsMultipleTransactions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
        try {
            return delegate.supportsNonNullableColumns();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        try {
            return delegate.supportsMinimumSQLGrammar();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
        try {
            return delegate.supportsCoreSQLGrammar();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }
    
    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        try {
            return delegate.supportsExtendedSQLGrammar();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        try {
            return delegate.supportsANSI92EntryLevelSQL();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        try {
            return delegate.supportsANSI92IntermediateSQL();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
        try {
            return delegate.supportsANSI92FullSQL();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        try {
            return delegate.supportsIntegrityEnhancementFacility();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsOuterJoins() throws SQLException {
        try {
            return delegate.supportsOuterJoins();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
        try {
            return delegate.supportsFullOuterJoins();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
        try {
            return delegate.supportsLimitedOuterJoins();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getSchemaTerm() throws SQLException {
        try {
            return delegate.getSchemaTerm();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getProcedureTerm() throws SQLException {
        try {
            return delegate.getProcedureTerm();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getCatalogTerm() throws SQLException {
        try {
            return delegate.getCatalogTerm();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean isCatalogAtStart() throws SQLException {
        try {
            return delegate.isCatalogAtStart();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public String getCatalogSeparator() throws SQLException {
        try {
            return delegate.getCatalogSeparator();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        try {
            return delegate.supportsSchemasInDataManipulation();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        try {
            return delegate.supportsSchemasInProcedureCalls();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        try {
            return delegate.supportsSchemasInTableDefinitions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        try {
            return delegate.supportsSchemasInIndexDefinitions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        try {
            return delegate.supportsSchemasInPrivilegeDefinitions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        try {
            return delegate.supportsCatalogsInDataManipulation();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        try {
            return delegate.supportsCatalogsInProcedureCalls();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        try {
            return delegate.supportsCatalogsInTableDefinitions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        try {
            return delegate.supportsCatalogsInIndexDefinitions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        try {
            return delegate.supportsCatalogsInPrivilegeDefinitions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsPositionedDelete() throws SQLException {
        try {
            return delegate.supportsPositionedDelete();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
        try {
            return delegate.supportsPositionedUpdate();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
        try {
            return delegate.supportsSelectForUpdate();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsStoredProcedures() throws SQLException {
        try {
            return delegate.supportsStoredProcedures();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        try {
            return delegate.supportsSubqueriesInComparisons();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
        try {
            return delegate.supportsSubqueriesInExists();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
        try {
            return delegate.supportsSubqueriesInIns();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        try {
            return delegate.supportsSubqueriesInQuantifieds();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        try {
            return delegate.supportsCorrelatedSubqueries();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsUnion() throws SQLException {
        try {
            return delegate.supportsUnion();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsUnionAll() throws SQLException {
        try {
            return delegate.supportsUnionAll();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        try {
            return delegate.supportsOpenCursorsAcrossCommit();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        try {
            return delegate.supportsOpenCursorsAcrossRollback();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        try {
            return delegate.supportsOpenStatementsAcrossCommit();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        try {
            return delegate.supportsOpenStatementsAcrossRollback();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
        try {
            return delegate.getMaxBinaryLiteralLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxCharLiteralLength() throws SQLException {
        try {
            return delegate.getMaxCharLiteralLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxColumnNameLength() throws SQLException {
        try {
            return delegate.getMaxColumnNameLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
        try {
            return delegate.getMaxColumnsInGroupBy();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxColumnsInIndex() throws SQLException {
        try {
            return delegate.getMaxColumnsInIndex();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
        try {
            return delegate.getMaxColumnsInOrderBy();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxColumnsInSelect() throws SQLException {
        try {
            return delegate.getMaxColumnsInSelect();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxColumnsInTable() throws SQLException {
        try {
            return delegate.getMaxColumnsInTable();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxConnections() throws SQLException {
        try {
            return delegate.getMaxConnections();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxCursorNameLength() throws SQLException {
        try {
            return delegate.getMaxCursorNameLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxIndexLength() throws SQLException {
        try {
            return delegate.getMaxIndexLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxSchemaNameLength() throws SQLException {
        try {
            return delegate.getMaxSchemaNameLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxProcedureNameLength() throws SQLException {
        try {
            return delegate.getMaxProcedureNameLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxCatalogNameLength() throws SQLException {
        try {
            return delegate.getMaxCatalogNameLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxRowSize() throws SQLException {
        try {
            return delegate.getMaxRowSize();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        try {
            return delegate.doesMaxRowSizeIncludeBlobs();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxStatementLength() throws SQLException {
        try {
            return delegate.getMaxStatementLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxStatements() throws SQLException {
        try {
            return delegate.getMaxStatements();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxTableNameLength() throws SQLException {
        try {
            return delegate.getMaxTableNameLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxTablesInSelect() throws SQLException {
        try {
            return delegate.getMaxTablesInSelect();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getMaxUserNameLength() throws SQLException {
        try {
            return delegate.getMaxUserNameLength();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
        try {
            return delegate.getDefaultTransactionIsolation();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsTransactions() throws SQLException {
        try {
            return delegate.supportsTransactions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        try {
            return delegate.supportsTransactionIsolationLevel(level);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        try {
            return delegate.supportsDataDefinitionAndDataManipulationTransactions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        try {
            return delegate.supportsDataManipulationTransactionsOnly();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        try {
            return delegate.dataDefinitionCausesTransactionCommit();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        try {
            return delegate.dataDefinitionIgnoredInTransactions();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        try {
            return delegate.getProcedures(catalog, schemaPattern, procedureNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        try {
            return delegate.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        try {
            return delegate.getTables(catalog, schemaPattern, tableNamePattern, types);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        try {
            return delegate.getSchemas();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        try {
            return delegate.getCatalogs();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        try {
            return delegate.getTableTypes();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        try {
            return delegate.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        try {
            return delegate.getColumnPrivileges(catalog, schema, table, columnNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        try {
            return delegate.getTablePrivileges(catalog, schemaPattern, tableNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        try {
            return delegate.getBestRowIdentifier(catalog, schema, table, scope, nullable);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        try {
            return delegate.getVersionColumns(catalog, schema, table);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        try {
            return delegate.getPrimaryKeys(catalog, schema, table);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        try {
            return delegate.getImportedKeys(catalog, schema, table);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        try {
            return delegate.getExportedKeys(catalog, schema, table);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        try {
            return delegate.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        try {
            return delegate.getTypeInfo();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        try {
            return delegate.getIndexInfo(catalog, schema, table, unique, approximate);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsResultSetType(int type) throws SQLException {
        try {
            return delegate.supportsResultSetType(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        try {
            return delegate.supportsResultSetConcurrency(type, concurrency);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        try {
            return delegate.ownUpdatesAreVisible(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        try {
            return delegate.ownDeletesAreVisible(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean ownInsertsAreVisible(int type) throws SQLException {
        try {
            return delegate.ownInsertsAreVisible(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        try {
            return delegate.othersUpdatesAreVisible(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean othersDeletesAreVisible(int type) throws SQLException {
        try {
            return delegate.othersDeletesAreVisible(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean othersInsertsAreVisible(int type) throws SQLException {
        try {
            return delegate.othersInsertsAreVisible(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean updatesAreDetected(int type) throws SQLException {
        try {
            return delegate.updatesAreDetected(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean deletesAreDetected(int type) throws SQLException {
        try {
            return delegate.deletesAreDetected(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean insertsAreDetected(int type) throws SQLException {
        try {
            return delegate.insertsAreDetected(type);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsBatchUpdates() throws SQLException {
        try {
            return delegate.supportsBatchUpdates();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        try {
            return delegate.getUDTs(catalog, schemaPattern, typeNamePattern, types);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            return delegate.getConnection();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSavepoints() throws SQLException {
        try {
            return delegate.supportsSavepoints();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsNamedParameters() throws SQLException {
        try {
            return delegate.supportsNamedParameters();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
        try {
            return delegate.supportsMultipleOpenResults();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
        try {
            return delegate.supportsGetGeneratedKeys();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        try {
            return delegate.getSuperTypes(catalog, schemaPattern, typeNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        try {
            return delegate.getSuperTables(catalog, schemaPattern, tableNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        try {
            return delegate.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        try {
            return delegate.supportsResultSetHoldability(holdability);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        try {
            return delegate.getResultSetHoldability();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        try {
            return delegate.getDatabaseMajorVersion();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        try {
            return delegate.getDatabaseMinorVersion();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getJDBCMajorVersion() throws SQLException {
        try {
            return delegate.getJDBCMajorVersion();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getJDBCMinorVersion() throws SQLException {
        try {
            return delegate.getJDBCMinorVersion();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public int getSQLStateType() throws SQLException {
        try {
            return delegate.getSQLStateType();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
        try {
            return delegate.locatorsUpdateCopy();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsStatementPooling() throws SQLException {
        try {
            return delegate.supportsStatementPooling();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        try {
            return delegate.getRowIdLifetime();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        try {
            return delegate.getSchemas(catalog, schemaPattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        try {
            return delegate.supportsStoredFunctionsUsingCallSyntax();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        try {
            return delegate.autoCommitFailureClosesAllResultSets();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        try {
            return delegate.getClientInfoProperties();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        try {
            return delegate.getFunctions(catalog, schemaPattern, functionNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
        try {
            return delegate.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        try {
            return delegate.getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        try {
            return delegate.generatedKeyAlwaysReturned();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public long getMaxLogicalLobSize() throws SQLException {
        try {
            return delegate.getMaxLogicalLobSize();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsRefCursors() throws SQLException {
        try {
            return delegate.supportsRefCursors();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean supportsSharding() throws SQLException {
        try {
            return delegate.supportsSharding();
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public Object unwrap(Class iface) throws SQLException {
        try {
            return delegate.unwrap(iface);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }

    @Override
    public boolean isWrapperFor(Class iface) throws SQLException {
        try {
            return delegate.isWrapperFor(iface);
        }
        catch (SQLException sqlException) {
            throw this.notionWrapper.handleSQLException(sqlException, this);
        }
    }
}
